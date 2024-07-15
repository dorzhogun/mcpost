package ru.skillbox.mcpost.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.skillbox.mcpost.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class NotificationEvent {
    @JsonProperty("userId")
    private UUID userId;
    @JsonProperty("authorId")
    private UUID authorId;
    @JsonProperty("NotificationType")
    private NotificationType notificationType;
    @JsonProperty("sentTime")
    private LocalDateTime sentTime;
    @JsonProperty("content")
    private String content;
    @JsonProperty("isStatusSent")
    private Boolean isStatusSent;
}
