package com.example.mcpost.dto;

import com.example.mcpost.model.enums.PostType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostDto {
    private Long id;
    private UUID authorId;
    private String title;
    private String postText;
    private LocalDateTime publishDate;
    private String imagePath;
    private List<TagDto> tags;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private PostType type;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private Integer commentsCount;
    private Integer likeAmount;
}
