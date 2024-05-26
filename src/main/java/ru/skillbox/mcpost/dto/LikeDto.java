package ru.skillbox.mcpost.dto;

import ru.skillbox.mcpost.model.enums.LikeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDto {
    private LikeType type;
    private String reactionType;
}
