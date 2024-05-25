package com.example.mcpost.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateJwtTokenRq {
    private String token;
}
