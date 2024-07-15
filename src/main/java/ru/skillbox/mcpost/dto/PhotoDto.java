package ru.skillbox.mcpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhotoDto {
    @JsonProperty("imagePath")
    private String imagePath;
}

