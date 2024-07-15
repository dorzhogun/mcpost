package ru.skillbox.mcpost.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time")
    private Instant time;
    @Column(name = "time_changed")
    private Instant timeChanged;
    @Column(name = "author_id")
    private UUID authorId;
    @Column(name = "comment_text")
    private String commentText;
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "is_blocked")
    private Boolean isBlocked;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @JoinColumn(name = "comment_id")
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CommentLike> likes;
    @JoinColumn(name = "parent_id")
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;
}