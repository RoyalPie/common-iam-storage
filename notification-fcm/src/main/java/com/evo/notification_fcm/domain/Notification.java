package com.evo.notification_fcm.domain;

import com.evo.notification_fcm.domain.command.StoredNotificationCmd;
import com.evo.notification_fcm.infrastructure.support.IdUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private UUID id;
    private UUID userId;
    private String title;
    private String body;
    private String topic;
    private String token;
    private Map<String, String> data;

    public Notification(StoredNotificationCmd cmd){
        this.id = IdUtils.newUUID();
        this.title = cmd.getTitle();
        this.body = cmd.getBody();
        this.topic = cmd.getTopic();
        this.token = cmd.getToken();
        this.data = cmd.getData();
    }
}
