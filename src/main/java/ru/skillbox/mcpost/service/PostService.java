package ru.skillbox.mcpost.service;

import ru.skillbox.mcpost.config.MvcConfig;
import ru.skillbox.mcpost.dto.*;
import ru.skillbox.mcpost.dto.criteria.CommentSearchCriteria;
import ru.skillbox.mcpost.dto.criteria.PostSearchCriteria;
import ru.skillbox.mcpost.exceptions.ResourceNotFoundException;
import ru.skillbox.mcpost.mapper.CommentMapper;
import ru.skillbox.mcpost.mapper.PostMapper;
import ru.skillbox.mcpost.mapper.TagMapper;
import ru.skillbox.mcpost.model.Comment;
import ru.skillbox.mcpost.model.CommentLike;
import ru.skillbox.mcpost.model.Post;
import ru.skillbox.mcpost.model.PostLike;
import ru.skillbox.mcpost.model.enums.PostType;
import ru.skillbox.mcpost.model.enums.ReactionType;
import ru.skillbox.mcpost.repository.CommentLikeRepository;
import ru.skillbox.mcpost.repository.CommentRepository;
import ru.skillbox.mcpost.repository.PostLikeRepository;
import ru.skillbox.mcpost.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MvcConfig config;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;

    public PhotoDto save(Principal principal,
                         String type,
                         MultipartFile file) {
        UUID principalId = UUID.fromString(principal.getName());
        String dir = config.getPath() + "/photo/" + principalId + "/";
        String filePath = dir + file.getOriginalFilename();

        try {
            Path dirPath = Paths.get(dir);
            if (Files.notExists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            file.transferTo(Paths.get(filePath));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PhotoDto("/photo/" + principalId + "/" + file.getOriginalFilename());
    }

    public PostDto createPost(Principal principal, PostDto postDto) {
        UUID principalId = UUID.fromString(principal.getName());
        postDto.setAuthorId(principalId);
        postDto.setTime(LocalDateTime.now());
        postDto.setTimeChanged(LocalDateTime.now());
        postDto.setType(
                postDto.getPublishDate() != null ? PostType.QUEUED : PostType.POSTED
        );
        postDto.setIsBlocked(false);
        postDto.setIsDeleted(false);
        return postMapper.toDto(postRepository.save(postMapper.toEntity(postDto)));
    }

    public PostDto updatePost(PostDto postDto) {
        Post post = getPostById(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setPostText(postDto.getPostText());
        post.setImagePath(postDto.getImagePath());
        post.setTags(tagMapper.toListEntity(postDto.getTags()));
        post.setPublishDate(postDto.getPublishDate());
        post.setType(postDto.getPublishDate() != null ? PostType.QUEUED : PostType.POSTED);
        post.setTimeChanged(LocalDateTime.now());
        return postMapper.toDto(postRepository.save(post));
    }

    public PostsRs getPosts(PostSearchCriteria postSearchCriteria, String sort, Integer page, Integer size) {
        String[] sorts = sort.split(",");
        Page<Post> posts = postRepository.findAll(
                postSearchCriteria.getSpecification(),
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.valueOf(sorts[1].toUpperCase()),
                                sorts[0].equals("time") ? "publishDate" : sorts[1]
                        )
                )
        );
        return new PostsRs(
                posts.getTotalPages(),
                posts.getTotalElements(),
                page,
                size,
                postMapper.toDtoList(posts.getContent())
        );
    }

    public void deletePost(Long id) {
        Post post = getPostById(id);
        post.setIsDeleted(true);
        postRepository.save(post);
    }

    private Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                MessageFormat.format("Пост с id: {0} не найден", id)
                        )
                );
    }

    public LikeDto createPostLike(Principal principal, Long postId, LikeDto likeDto) {
        UUID principalId = UUID.fromString(principal.getName());
        PostLike like = postLikeRepository.findByPostIdAndAuthorId(postId, principalId)
                .orElse(
                        PostLike.builder()
                                .authorId(principalId)
                                .postId(postId)
                                .isDeleted(false)
                                .build()
                );
        like.setReactionType(ReactionType.valueOf(likeDto.getReactionType().toUpperCase()));
        postLikeRepository.save(like);
        return likeDto;
    }

    public CommentsRs getPostComments(Principal principal,
                                      Long postId,
                                      String sort,
                                      Integer size,
                                      Integer page,
                                      Boolean isDeleted) {
        String[] sorts = sort.split(",");
        Page<Comment> postComments = commentRepository.findAll(
                new CommentSearchCriteria(postId, 0L, isDeleted).getSpecification(),
                PageRequest.of(
                        page, size,
                        Sort.by(Sort.Direction.valueOf(sorts[1].toUpperCase()), sorts[0])
                )
        );
        return new CommentsRs(
                postComments.getTotalPages(),
                postComments.getTotalElements(),
                page,
                size,
                commentMapper.toDtoList(postComments.getContent())
        );
    }

    public CommentDto createComments(Principal principal, Long postId, CommentRq commentRq) {
        UUID authorId = UUID.fromString(principal.getName());
        Comment comment = Comment.builder()
                .authorId(authorId)
                .postId(postId)
                .parentId(commentRq.getParentId())
                .commentText(commentRq.getCommentText())
                .isBlocked(false)
                .isDeleted(false)
                .time(LocalDateTime.now())
                .timeChanged(LocalDateTime.now())
                .build();
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public CommentDto setCommentLike(Principal principal, Long postId, Long commentId) {
        UUID authorId = UUID.fromString(principal.getName());
        CommentLike commentLike = commentLikeRepository
                .findByAuthorIdAndCommentId(authorId, commentId)
                .orElse(
                        CommentLike.builder()
                                .commentId(commentId)
                                .authorId(authorId)
                                .isDeleted(false)
                                .build()
                );
        commentLike.setReactionType(ReactionType.HEART);
        commentLike.setIsDeleted(false);
        commentLikeRepository.save(commentLike);
        return commentMapper.toDto(getCommentById(commentId));
    }

    public CommentDto removeCommentLike(Principal principal, Long postId, Long commentId) {
        UUID authorId = UUID.fromString(principal.getName());
        CommentLike commentLike = commentLikeRepository
                .findByAuthorIdAndCommentId(authorId, commentId)
                .orElseThrow();
        commentLike.setIsDeleted(true);
        commentLikeRepository.save(commentLike);
        return commentMapper.toDto(getCommentById(commentId));
    }

    public CommentsRs getCommentComments(Long postId, Long parentId, Integer page,
                                         Integer size, String sort, Boolean isDeleted) {
        String[] sorts = sort.split(",");
        Page<Comment> commentComments = commentRepository.findAll(
                new CommentSearchCriteria(postId, parentId, isDeleted).getSpecification(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sorts[1].toUpperCase()), sorts[0]))
        );
        return new CommentsRs(
                commentComments.getTotalPages(),
                commentComments.getTotalElements(),
                page,
                size,
                commentMapper.toDtoList(commentComments.getContent())
        );
    }

    public void removeComment(Long postId, Long commentId) {
        Comment comment = getCommentById(commentId);
        commentRepository.saveAll(
                Stream.concat(Stream.of(comment), comment.getComments().stream())
                        .peek(com -> com.setIsDeleted(true))
                        .toList()
        );
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format("Комментайрий с id {0} не найден", commentId)
                        )
                );
    }
}
