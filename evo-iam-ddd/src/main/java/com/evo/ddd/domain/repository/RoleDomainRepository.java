package com.evo.ddd.domain.repository;

import com.evo.common.domainRepository.DomainRepository;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.Role;

import java.util.List;
import java.util.UUID;

public interface RoleDomainRepository extends DomainRepository<Role, UUID> {
    Role findByName(String name);
    List<Permission> findPermissionByRoleId(UUID roleId);
}
