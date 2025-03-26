package com.evo.ddd.application.service.impl.command;

import com.evo.ddd.application.dto.mapper.RoleDTOMapper;
import com.evo.ddd.application.dto.request.CreateOrUpdateRoleRequest;
import com.evo.ddd.application.dto.response.RoleDTO;
import com.evo.ddd.application.mapper.CommandMapper;
import com.evo.ddd.application.service.RoleCommandService;
import com.evo.ddd.domain.Role;
import com.evo.ddd.domain.command.CreateOrUpdateRoleCmd;
import com.evo.ddd.domain.repository.RoleDomainRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleDTOMapper roleDTOMapper;
    private final CommandMapper commandMapper;
    private final RoleDomainRepository roleDomainRepository;

    @Override
    @Transactional
    public RoleDTO createRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        CreateOrUpdateRoleCmd createOrUpdateRoleCmd = commandMapper.from(createOrUpdateRoleRequest);
        Role role = new Role(createOrUpdateRoleCmd);
        role = roleDomainRepository.save(role);
        return roleDTOMapper.domainModelToDTO(role);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        CreateOrUpdateRoleCmd createOrUpdateRoleCmd = commandMapper.from(createOrUpdateRoleRequest);
        Role role = roleDomainRepository.getById(createOrUpdateRoleCmd.getId());
        role.update(createOrUpdateRoleCmd);
        role = roleDomainRepository.save(role);
        return roleDTOMapper.domainModelToDTO(role);
    }
}
