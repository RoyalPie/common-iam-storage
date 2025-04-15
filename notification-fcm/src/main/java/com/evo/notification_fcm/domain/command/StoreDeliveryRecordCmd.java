package com.evo.notification_fcm.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class StoreDeliveryRecordCmd {
    private UUID notificationId;
    private UUID deviceRegistrationId;
    private String status;
    private Instant sendAt;
}
