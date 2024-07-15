package ru.skillbox.mcpost.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.skillbox.mcpost.model.enums.PostType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author_id")
    private UUID authorId;
    @Column(name = "title")
    private String title;
    @Column(name = "post_text")
    private String postText;
    @Column(name = "publish_date")
    private Instant publishDate;
    @Column(name = "image_path")
    private String imagePath;
    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
    @Column(name = "time")
    private Instant time;
    @Column(name = "time_changed")
    private Instant timeChanged;
    @Enumerated(EnumType.STRING)
    private PostType type;
    @Column(name = "is_blocked")
    private Boolean isBlocked;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @JoinColumn(name = "post_id")
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PostLike> likes;
    @JoinColumn(name = "post_id")
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;
}

