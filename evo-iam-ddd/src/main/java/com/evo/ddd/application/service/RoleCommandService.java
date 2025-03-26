package com.evo.ddd.application.service;

import com.evo.ddd.application.dto.request.CreateOrUpdateRoleRequest;
import com.evo.ddd.application.dto.response.RoleDTO;

public interface RoleCommandService {
    RoleDTO createRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest);
    RoleDTO updateRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest);
}
