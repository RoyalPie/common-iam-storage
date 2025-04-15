package com.evo.notification_fcm.domain;

import com.evo.notification_fcm.domain.command.StoreDeliveryRecordCmd;
import com.evo.notification_fcm.infrastructure.support.IdUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryRecord {
    private UUID id;
    private UUID notificationId;
    private UUID deviceRegistrationId;
    private String status;
    private Instant sendAt;

    public DeliveryRecord(StoreDeliveryRecordCmd cmd) {
        this.id = IdUtils.newUUID();
        this.notificationId = cmd.getNotificationId();
        this.deviceRegistrationId = cmd.getDeviceRegistrationId();
        this.status = cmd.getStatus();
        this.sendAt = cmd.getSendAt();
    }
}
