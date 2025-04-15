package com.evo.common.dto.event;

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
public class PushNotificationEvent {
    private String title;
    private String body;
    private String imageUrl;
    private String topic;
    private UUID userId;
    private String token;
    private Map<String, String> data;
}