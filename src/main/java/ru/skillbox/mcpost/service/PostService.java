package ru.skillbox.mcpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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

import java.security.Principal;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;

    public PostDto createPost(Principal principal, PostRq postRq) {
        PostDto postDto = new PostDto();
        postDto.setAuthorId(UUID.fromString(principal.getName()));
        postDto.setImagePath(postRq.getImagePath());
        postDto.setPostText(postRq.getPostText());
        postDto.setTitle(postRq.getTitle());
        postDto.setTime(postRq.getTime());
        postDto.setTimeChanged(postRq.getTimeChanged());
        postDto.setPublishDate(postRq.getPublishDate());
        postDto.setTags(
                postRq.getTags() != null ? postRq.getTags().stream().map(tagMapper::toDto).collect(Collectors.toList()) : new ArrayList<>()
        );
        postDto.setType(
                postRq.getPublishDate() != null ? PostType.POSTED : PostType.QUEUED
        );
        postDto.setIsBlocked(false);
        postDto.setIsDeleted(false);
        return postMapper.toDto(postRepository.save(postMapper.toEntity(postDto)));
    }

    public PostDto updatePost(PostDto postDto) {
        Post post = postMapper.toEntity(getPostById(postDto.getId()));
        post.setTitle(postDto.getTitle());
        post.setPostText(postDto.getPostText());
        post.setImagePath(postDto.getImagePath());
        post.setTags(tagMapper.toListEntity(postDto.getTags()));
        post.setPublishDate(postDto.getPublishDate());
        post.setType(postDto.getPublishDate() != null ? PostType.POSTED : PostType.QUEUED);
        post.setTimeChanged(postDto.getTimeChanged());
        return postMapper.toDto(postRepository.save(post));
    }

    public PostsRs getPosts(PostSearchCriteria postSearchCriteria, String sort, Integer page, Integer size) {
        publishPost();
        String[] sorts = sort.split(",");
        Page<Post> posts = postRepository.findAll(
                postSearchCriteria.getSpecification(),
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.valueOf(sorts[1].toUpperCase()),
                                sorts[0].equals("time") ? "timeChanged" : sorts[0]
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
        Post post = postMapper.toEntity(getPostById(id));
        post.setIsDeleted(true);
        postRepository.save(post);
    }

    public PostDto getPostById(Long id) {
        return postMapper.toDto(postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                MessageFormat.format("Пост с id: {0} не найден", id)
                        )
                ));
    }

    public LikeDto createPostLike(Principal principal, Long postId, LikeDto likeDto) {
        UUID principalId = UUID.fromString(principal.getName());
        PostLike like = postLikeRepository.findByPostIdAndAuthorId(postId, principalId)
                .orElse(
                        PostLike.builder().authorId(principalId)
                                .postId(postId)
                                .build()
                );
        like.setReactionType(ReactionType.valueOf(likeDto.getReactionType().toUpperCase()));
        like.setIsDeleted(false);
        postLikeRepository.save(like);
        return likeDto;
    }

    public PostDto removePostLike(Principal principal, Long postId) {
        UUID authorId = UUID.fromString(principal.getName());
        PostLike postLike = postLikeRepository.findByPostIdAndAuthorId(postId, authorId).orElseThrow();
        postLike.setIsDeleted(true);
        postLikeRepository.save(postLike);
        return getPostById(postId);
    }

    public CommentsRs getPostComments(Long postId,
                                      String sort,
                                      Integer size,
                                      Integer page,
                                      Boolean isDeleted) {
        String[] sorts = sort != null ? sort.split(",") : new String[] {"time", "DESC"};
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
                .time(Instant.now())
                .timeChanged(Instant.now())
                .build();
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public CommentDto setCommentLike(Principal principal, Long commentId) {
        UUID authorId = UUID.fromString(principal.getName());
        CommentLike commentLike = commentLikeRepository
                .findByAuthorIdAndCommentId(authorId, commentId)
                .orElse(
                        CommentLike.builder()
                                .commentId(commentId)
                                .authorId(authorId)
                                .build()
                );
        commentLike.setReactionType(ReactionType.HEART);
        commentLike.setIsDeleted(false);
        commentLikeRepository.save(commentLike);
        return commentMapper.toDto(getCommentById(commentId));
    }

    public CommentDto removeCommentLike(Principal principal, Long commentId) {
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
        String[] sorts = sort != null ? sort.split(",") : new String[] {"time", "DESC"};
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

    public void removeComment(Long commentId) {
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

    private void publishPost() {
        postRepository.saveAll(
                postRepository.findAll(getSpecification())
                        .stream()
                        .peek(post -> {
                            post.setType(PostType.POSTED);
                            post.setTimeChanged(Instant.now());
                        })
                        .toList()
        );
    }

    private static Specification<Post> getSpecification() {
        return Specification.allOf(
                (root, query, cb) -> cb.equal(root.get("type"), PostType.QUEUED),
                (root, query, cb) -> cb.lessThanOrEqualTo(root.get("publishDate"), Instant.now())
        );
    }
}
