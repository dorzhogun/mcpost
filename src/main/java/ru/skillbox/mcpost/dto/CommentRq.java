package ru.skillbox.mcpost.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRq {
    private Long parentId;
    private String commentText;
}
