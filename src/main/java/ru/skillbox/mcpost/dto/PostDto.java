package ru.skillbox.mcpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.skillbox.mcpost.model.enums.PostType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("authorId")
    private UUID authorId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("postText")
    private String postText;
    @JsonProperty("publishDate")
    private Instant publishDate;
    @JsonProperty("imagePath")
    private String imagePath;
    @JsonProperty("tags")
    private List<TagDto> tags;
    @JsonProperty("time")
    private Instant time;
    @JsonProperty("timeChanged")
    private Instant timeChanged;
    @JsonProperty("type")
    private PostType type;
    @JsonProperty("isBlocked")
    private Boolean isBlocked;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
    @JsonProperty("commentsCount")
    private Integer commentsCount;
    @JsonProperty("likeAmount")
    private Integer likeAmount;
}
