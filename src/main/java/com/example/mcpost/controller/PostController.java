package com.example.mcpost.controller;

import com.example.mcpost.dto.*;
import com.example.mcpost.dto.criteria.PostSearchCriteria;
import com.example.mcpost.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService service;

    @GetMapping("")
    public PostsRs getPosts(@RequestParam(required = false) String ids,
                            @RequestParam(required = false) String accountIds,
                            @RequestParam(required = false) String blockedIds,
                            @RequestParam(required = false) Boolean isDeleted,
                            @RequestParam(required = false) Boolean withFriends,
                            @RequestParam(required = false) String author,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String postText,
                            @RequestParam(required = false) String tags,
                            @RequestParam(required = false) LocalDateTime dateFrom,
                            @RequestParam(required = false) LocalDateTime dateTo,
                            @RequestParam(required = false) String sort,
                            @RequestParam(required = false, defaultValue = "0") Integer page,
                            @RequestParam(required = false, defaultValue = "5") Integer size) {
        return service.getPosts(
                new PostSearchCriteria(
                        ids, accountIds, blockedIds, isDeleted, withFriends,
                        author, title, postText, tags, dateFrom, dateTo
                ),
                sort, page, size
        );
    }

    @PostMapping("/storagePostPhoto")
    public PhotoDto save(Principal principal,
                         @RequestParam String type,
                         @RequestBody MultipartFile file) {

        return service.save(principal, type, file);
    }

    @PostMapping("")
    public PostDto createPost(Principal principal, @RequestBody PostDto postDto) {
        return service.createPost(principal, postDto);
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

    @GetMapping("/{id}/comment")
    public CommentsRs getPostComments(Principal principal,
                                      @PathVariable Long id,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(required = false, defaultValue = "5") Integer size,
                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                      @RequestParam Boolean isDeleted) {
        return service.getPostComments(principal, id, sort, size, page, isDeleted);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(Principal principal,
                                    @PathVariable Long id,
                                    @RequestBody CommentRq commentRq) {
        return service.createComments(principal, id, commentRq);
    }

    @PostMapping("/{postId}/comment/{postCommentId}/like")
    public CommentDto setPostCommentLike(Principal principal,
                                         @PathVariable Long postId,
                                         @PathVariable Long postCommentId) {
        return service.setCommentLike(principal, postId, postCommentId);
    }

    @DeleteMapping("/{postId}/comment/{commentId}/like")
    public CommentDto removeCommentLike(Principal principal,
                                        @PathVariable Long postId,
                                        @PathVariable Long commentId) {
        return service.removeCommentLike(principal, postId, commentId);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public void removeComment(@PathVariable Long postId,
                              @PathVariable Long commentId) {
        service.removeComment(postId, commentId);
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
