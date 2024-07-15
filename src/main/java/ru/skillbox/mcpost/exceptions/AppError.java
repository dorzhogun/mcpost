package ru.skillbox.mcpost.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppError {
    @JsonProperty("statusCode")
    private int statusCode;
    @JsonProperty("message")
    private String message;
}
