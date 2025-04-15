package com.evo.notification_fcm.domain;

import com.evo.notification_fcm.domain.command.RegisterOrUpdateDeviceCmd;
import com.evo.notification_fcm.domain.command.StoreDeliveryRecordCmd;
import com.evo.notification_fcm.infrastructure.support.IdUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDevice {
    private UUID id;
    private UUID userId;
    private String deviceToken;
    private UUID deviceId;
    private Instant lastLoginAt;
    private boolean enabled;
    private List<DeliveryRecord> deliveryRecords;


    public UserDevice(RegisterOrUpdateDeviceCmd cmd) {
        this.id = IdUtils.newUUID();
        this.userId = cmd.getUserId();
        this.deviceToken = cmd.getDeviceToken();
        this.deviceId = cmd.getDeviceId();
        this.enabled = cmd.isEnabled();
    }

    public void addNotificationDelivery(StoreDeliveryRecordCmd cmd) {
        DeliveryRecord deliveryRecord = new DeliveryRecord(cmd);
        if (this.deliveryRecords == null) {
            this.deliveryRecords = new ArrayList<>();
        }
        this.deliveryRecords.add(deliveryRecord);
    }
}
