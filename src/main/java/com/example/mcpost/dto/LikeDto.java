package com.example.mcpost.dto;

import com.example.mcpost.model.enums.LikeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDto {
    private LikeType type;
    private String reactionType;
}
