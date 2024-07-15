package ru.skillbox.mcpost.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import ru.skillbox.mcpost.dto.*;
import ru.skillbox.mcpost.dto.criteria.PostSearchCriteria;
import ru.skillbox.mcpost.feign.PostFeignClient;
import ru.skillbox.mcpost.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneId;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService service;
    private final PostFeignClient feignClient;

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable Long id) {
        return service.getPostById(id);
    }

    @GetMapping("")
    public PostsRs getPosts(HttpServletRequest request,
                            @RequestParam(required = false) String ids,
                            @RequestParam(required = false) String accountIds,
                            @RequestParam(required = false, defaultValue = "false") Boolean isBlocked,
                            @RequestParam(required = false, defaultValue = "false") Boolean isDeleted,
                            @RequestParam(required = false, defaultValue = "false") Boolean withFriends,
                            @RequestParam(required = false) String author,
                            @RequestParam(required = false) String text,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String tags,
                            @RequestParam(required = false, defaultValue = "1970-01-01T03:00:00.000Z") Instant dateFrom,
                            @RequestParam(required = false, defaultValue = "2070-01-01T03:00:00.000Z") Instant dateTo,
                            @RequestParam(required = false, defaultValue = "time,desc") String sort,
                            @RequestParam(required = false, defaultValue = "0") Integer page,
                            @RequestParam(required = false, defaultValue = "10") Integer size) {

        return service.getPosts(
                PostSearchCriteria.builder()
                        .ids(ids).accountIds(accountIds).isBlocked(isBlocked).isDeleted(isDeleted)
                        .withFriends(withFriends).title(title).text(text).tags(tags)
                        .dateFrom(dateFrom.atZone(ZoneId.systemDefault()).toInstant())
                        .dateTo(dateTo.atZone(ZoneId.systemDefault()).toInstant())
                        .authorIds(
                                author != null ?
                                        feignClient.getAccounts(
                                                        author, size, request.getHeader(HttpHeaders.AUTHORIZATION)
                                                )
                                                .getContent()
                                                .stream()
                                                .map(AccountDto::getId)
                                                .toList() : null
                        )
                        .build(),
                sort, page, size
        );
    }

    @PostMapping("/storagePostPhoto")
    public PhotoDto save(HttpServletRequest request,
                         Principal principal,
                         @RequestParam(required = false, defaultValue = "") String type,
                         @RequestBody MultipartFile file) {

        return new PhotoDto(
                feignClient.upload(principal.getName(), type, file, request.getHeader(HttpHeaders.COOKIE))
        );
    }

    @PostMapping("")
    public PostDto createPost(Principal principal, @RequestBody PostRq postRq) {
        return service.createPost(principal, postRq);
    }

    @PutMapping("")
    public PostDto updatePost(@RequestBody PostDto postDto) {
        return service.updatePost(postDto);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        service.deletePost(id);
    }

    @PostMapping("/{id}/like")
    public LikeDto createPostLike(Principal principal, @PathVariable Long id, @RequestBody LikeDto likeDto) {
        return service.createPostLike(principal, id, likeDto);
    }

    @DeleteMapping("/{id}/like")
    public PostDto deletePostLike(Principal principal,
                                  @PathVariable Long id) {
        return service.removePostLike(principal, id);
    }

    @GetMapping("/{id}/comment")
    public CommentsRs getPostComments(@PathVariable Long id,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(required = false, defaultValue = "5") Integer size,
                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                      @RequestParam(required = false) Boolean isDeleted) {
        return service.getPostComments(id, sort, size, page, isDeleted);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(Principal principal,
                                    @PathVariable Long id,
                                    @RequestBody CommentRq commentRq) {
        return service.createComments(principal, id, commentRq);
    }

    @PostMapping("/{postId}/comment/{postCommentId}/like")
    public CommentDto setPostCommentLike(Principal principal,
                                         @PathVariable Long postCommentId) {
        return service.setCommentLike(principal, postCommentId);
    }

    @DeleteMapping("/{postId}/comment/{commentId}/like")
    public CommentDto removeCommentLike(Principal principal,
                                        @PathVariable Long commentId) {
        return service.removeCommentLike(principal, commentId);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public void removeComment(@PathVariable Long commentId) {
        service.removeComment(commentId);
    }

    @GetMapping("/{postId}/comment/{commentId}/subcomment")
    public CommentsRs getCommentComments(@PathVariable Long postId,
                                         @PathVariable Long commentId,
                                         @RequestParam(required = false, defaultValue = "0") Integer page,
                                         @RequestParam(required = false, defaultValue = "5") Integer size,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false) Boolean isDeleted) {
        return service.getCommentComments(postId, commentId, page, size, sort, isDeleted);
    }
}