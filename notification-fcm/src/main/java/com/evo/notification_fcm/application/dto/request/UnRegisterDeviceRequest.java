package com.evo.notification_fcm.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnRegisterDeviceRequest {
    private UUID userId;
    private UUID deviceId;
    private String deviceToken;
}