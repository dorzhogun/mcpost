package ru.skillbox.mcpost.dto;

import ru.skillbox.mcpost.model.enums.CommentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private CommentType commentType;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private UUID authorId;
    private Long parentId;
    private String commentText;
    private Long postId;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private Integer likeAmount;
    private Integer commentsCount;
}
