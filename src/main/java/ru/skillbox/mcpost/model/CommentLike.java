package ru.skillbox.mcpost.model;

import ru.skillbox.mcpost.model.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "comment_likes",
        indexes = {
                @Index(name = "comment_like_author_id", columnList = "author_id")
        }
)
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author_id")
    private UUID authorId;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    @Column(name = "comment_id")
    private Long commentId;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
}

