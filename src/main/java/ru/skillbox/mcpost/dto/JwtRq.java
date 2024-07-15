package ru.skillbox.mcpost.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRq {
    private String token;
}
