package ru.skillbox.mcpost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("about")
    private String about;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("statusCode")
    private String statusCode;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("regDate")
    private String regDate;
    @JsonProperty("birthDate")
    private String birthDate;
    @JsonProperty("messagePermission")
    private String messagePermission;
    @JsonProperty("lastOnlineTime")
    private String lastOnlineTime;
    @JsonProperty("isOnline")
    private boolean isOnline;
    @JsonProperty("isBlocked")
    private boolean isBlocked;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("profileCover")
    private String profileCover;
    @JsonProperty("role")
    private String role;
    @JsonProperty("updateOn")
    private String updatedOn;
}
