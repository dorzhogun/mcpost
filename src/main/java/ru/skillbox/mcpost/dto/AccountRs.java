package ru.skillbox.mcpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRs {
    @JsonProperty("totalElements")
    private Long totalElements;
    @JsonProperty("totalPages")
    private Integer totalPages;
    @JsonProperty("content")
    private List<AccountDto> content;
}

