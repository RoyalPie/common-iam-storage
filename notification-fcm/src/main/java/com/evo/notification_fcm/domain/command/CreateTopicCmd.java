package com.evo.notification_fcm.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateTopicCmd {
    private UUID userId;
    private String topic;
    private boolean enabled;
}
