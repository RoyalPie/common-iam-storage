package com.evo.notification_fcm.domain;

import com.evo.notification_fcm.domain.command.CreateTopicCmd;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicSubscription {
    private UUID id;
    private UUID userId;
    private String topic;
    private boolean enabled;

    public TopicSubscription(CreateTopicCmd cmd) {
        this.userId = cmd.getUserId();
        this.topic = cmd.getTopic();
        this.enabled = cmd.isEnabled();
    }
}
