package com.evo.notification_fcm.domain.command;

import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class StoredNotificationCmd {
    private String title;
    private String body;
    private String topic;
    private String token;
    private Map<String, String> data;
}
