package com.example.mcpost.model;

import com.example.mcpost.model.enums.ReactionType;
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
        name = "post_likes",
        indexes = {
                @Index(name = "post_like_author_id", columnList = "author_id")
        }
)
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author_id")
    private UUID authorId;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    @Column(name = "post_id")
    private Long postId;
}

