package ru.skillbox.mcpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.mcpost.model.Tag;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRq {
    @JsonProperty("title")
    private String title;
    @JsonProperty("postText")
    private String postText;
    @JsonProperty("publishDate")
    private Instant publishDate;
    @JsonProperty("imagePath")
    private String imagePath;
    @JsonProperty("time")
    private Instant time;
    @JsonProperty("timeChanged")
    private Instant timeChanged;
    @JsonProperty("tags")
    private List<Tag> tags;
}
