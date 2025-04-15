package com.evo.notification_fcm.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class RegisterOrUpdateDeviceCmd {
    private UUID id;
    private UUID userId;
    private String deviceToken;
    private UUID deviceId;
    private Instant lastLoginAt;
    private boolean enabled;
}
