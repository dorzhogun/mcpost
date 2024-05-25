package com.example.mcpost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentsRs {
    private Integer totalPages;
    private Long totalElements;
    private Integer number;
    private Integer size;
    private List<CommentDto> content;
}
