package com.evo.ddd.application.service;

import com.evo.ddd.application.dto.request.CreateOrUpdatePermissionRequest;
import com.evo.ddd.application.dto.response.PermissionDTO;

public interface PermissionCommandService {
    PermissionDTO createPermission(CreateOrUpdatePermissionRequest request);
    PermissionDTO updatePermission(CreateOrUpdatePermissionRequest request);

}
