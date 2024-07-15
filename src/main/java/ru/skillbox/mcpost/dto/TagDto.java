package ru.skillbox.mcpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
}
