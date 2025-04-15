package com.evo.common.dto.event;

import com.evo.common.enums.TemplateCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendNotificationEvent {
    private String channel;
    private String recipient;
    private TemplateCode templateCode;
    private Map<String, Object> param;
}