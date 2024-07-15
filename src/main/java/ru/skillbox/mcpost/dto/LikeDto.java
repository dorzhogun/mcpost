package ru.skillbox.mcpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.skillbox.mcpost.model.enums.LikeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {
    @JsonProperty("type")
    private LikeType type;
    @JsonProperty("reactionType")
    private String reactionType;
}
