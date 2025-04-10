package com.evo.common.dto.event;

import com.evo.common.dto.request.SyncUserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncUserEvent {
    private String syncAction;
    private SyncUserRequest syncUserRequest;
}
