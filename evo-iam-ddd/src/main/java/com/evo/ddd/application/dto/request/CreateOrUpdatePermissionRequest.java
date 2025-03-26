package com.evo.ddd.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdatePermissionRequest {
    private UUID id;
    private String resource;
    private String scope;
}

