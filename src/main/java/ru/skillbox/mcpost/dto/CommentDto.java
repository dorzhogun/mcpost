package ru.skillbox.mcpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.skillbox.mcpost.model.enums.CommentType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class CommentDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("commentType")
    private CommentType commentType;
    @JsonProperty("time")
    private Instant time;
    @JsonProperty("timeChanged")
    private Instant timeChanged;
    @JsonProperty("authorId")
    private UUID authorId;
    @JsonProperty("parentId")
    private Long parentId;
    @JsonProperty("commentText")
    private String commentText;
    @JsonProperty("postId")
    private Long postId;
    @JsonProperty("isBlocked")
    private Boolean isBlocked;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
    @JsonProperty("likeAmount")
    private Integer likeAmount;
    @JsonProperty("commentsCount")
    private Integer commentsCount;
}
