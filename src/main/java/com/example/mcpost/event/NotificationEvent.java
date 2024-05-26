package com.example.mcpost.event;

import com.example.mcpost.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class NotificationEvent {
    private UUID userId;
    private UUID authorId;
    private NotificationType notificationType;
    private LocalDateTime sentTime;
    private String content;
    private Boolean isStatusSent;
}
