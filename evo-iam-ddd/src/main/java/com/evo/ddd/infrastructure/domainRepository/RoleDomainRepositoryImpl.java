package com.evo.ddd.infrastructure.domainRepository;

import com.evo.common.domainRepository.AbstractDomainRepository;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.Role;
import com.evo.ddd.domain.RolePermission;
import com.evo.ddd.domain.repository.RoleDomainRepository;
import com.evo.ddd.infrastructure.persistence.entity.PermissionEntity;
import com.evo.ddd.infrastructure.persistence.entity.RoleEntity;
import com.evo.ddd.infrastructure.persistence.entity.RolePermissionEntity;
import com.evo.ddd.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.evo.ddd.infrastructure.persistence.mapper.RoleEntityMapper;
import com.evo.ddd.infrastructure.persistence.mapper.RolePermissionEntityMapper;
import com.evo.ddd.infrastructure.persistence.repository.PermissionEntityRepository;
import com.evo.ddd.infrastructure.persistence.repository.RoleEntityRepository;
import com.evo.ddd.infrastructure.persistence.repository.RolePermissionEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RoleDomainRepositoryImpl extends AbstractDomainRepository<Role, RoleEntity, UUID>
        implements RoleDomainRepository {
    private final RoleEntityMapper roleEntityMapper;
    private final RoleEntityRepository roleEntityRepository;
    private  final RolePermissionEntityRepository rolePermissionEntityRepository;
    private final PermissionEntityMapper permissionEntityMapper;
    private final PermissionEntityRepository permissionEntityRepository;
    private final RolePermissionEntityMapper rolePermissionEntityMapper;

    public RoleDomainRepositoryImpl(RoleEntityRepository roleEntityRepository,
                                    RoleEntityMapper roleEntityMapper,
                                    PermissionEntityRepository permissionDomainRepository,
                                    PermissionEntityMapper permissionEntityMapper,
                                    RolePermissionEntityRepository rolePermissionEntityRepository,
                                    PermissionEntityRepository permissionEntityRepository,
                                    RolePermissionEntityMapper rolePermissionEntityMapper) {
        super(roleEntityRepository, roleEntityMapper);
        this.roleEntityRepository = roleEntityRepository;
        this.roleEntityMapper = roleEntityMapper;
        this.rolePermissionEntityRepository = rolePermissionEntityRepository;
        this.permissionEntityMapper = permissionEntityMapper;
        this.permissionEntityRepository = permissionEntityRepository;
        this.rolePermissionEntityMapper = rolePermissionEntityMapper;
    }


    @Override
    public List<Permission> findPermissionByRoleId(UUID roleId) {
        List<PermissionEntity> permissionEntities = permissionEntityRepository.findPermissionByRoleId(roleId);
        return permissionEntityMapper.toDomainModelList(permissionEntities);
    }

    @Override
    @Transactional
    public Role save(Role domainModel) {
        RoleEntity roleEntity = roleEntityMapper.toEntity(domainModel);
        List<RolePermissionEntity> rolePermissionEntities = rolePermissionEntityMapper.toEntityList(domainModel.getRolePermissions());
        rolePermissionEntityRepository.saveAll(rolePermissionEntities);
        return roleEntityMapper.toDomainModel(roleEntityRepository.save(roleEntity));
    }

    @Override
    public Role getById(UUID uuid) {
        RoleEntity roleEntity =  roleEntityRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Role not found"));
        return enrich(roleEntityMapper.toDomainModel(roleEntity));
    }


    @Override
    public Role findByName(String name) {
        RoleEntity roleEntity = roleEntityRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
        return roleEntityMapper.toDomainModel(roleEntity);
    }

    @Override
    protected List<Role> enrichList(List<Role> roles) {
        if (roles.isEmpty()) return roles;

        List<UUID> roleIds = roles.stream().map(Role::getId).toList();
        Map<UUID, List<RolePermission>> rolePermissionMap = rolePermissionEntityRepository.findByRoleIdInAndDeletedFalse(roleIds)
                .stream()
                .collect(Collectors.groupingBy(
                        RolePermissionEntity::getRoleId,
                        Collectors.mapping(rolePermissionEntityMapper::toDomainModel, Collectors.toList())
                ));

        roles.forEach(role -> role.setRolePermissions(new ArrayList<>(rolePermissionMap.getOrDefault(role.getId(), Collections.emptyList()))));
        return roles;
    }
}
