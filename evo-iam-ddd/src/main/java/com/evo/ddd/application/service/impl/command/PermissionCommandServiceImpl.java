package com.evo.ddd.application.service.impl.command;

import com.evo.ddd.application.dto.mapper.PermissionDTOMapper;
import com.evo.ddd.application.dto.request.CreateOrUpdatePermissionRequest;
import com.evo.ddd.application.dto.response.PermissionDTO;
import com.evo.ddd.application.mapper.CommandMapper;
import com.evo.ddd.application.service.PermissionCommandService;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.command.CreateOrUpdatePermissionCmd;
import com.evo.ddd.infrastructure.domainRepository.PermissionDomainRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionCommandServiceImpl implements PermissionCommandService {
    private final PermissionDomainRepositoryImpl permissionDomainRepositoryImpl;
    private final PermissionDTOMapper permissionDTOMapper;
    private final CommandMapper commandMapper;

    @Override
    public PermissionDTO createPermission(CreateOrUpdatePermissionRequest request) {
        CreateOrUpdatePermissionCmd cmd = commandMapper.from(request);
        Permission permission = new Permission(cmd);
        return permissionDTOMapper.domainModelToDTO(permissionDomainRepositoryImpl.save(permission));
    }

    @Override
    public PermissionDTO updatePermission(CreateOrUpdatePermissionRequest request) {
        CreateOrUpdatePermissionCmd cmd = commandMapper.from(request);
        Permission permission = permissionDomainRepositoryImpl.getById(cmd.getId());
        permission.update(cmd);
        return permissionDTOMapper.domainModelToDTO(permissionDomainRepositoryImpl.save(permission));
    }
}
