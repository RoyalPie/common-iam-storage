package com.evo.ddd.domain.repository;

import com.evo.common.domainRepository.DomainRepository;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.query.SearchPermissionQuery;

import java.util.List;
import java.util.UUID;

public interface PermissionDomainRepository extends DomainRepository<Permission, UUID> {
    List<Permission> findPermissionByRoleId(UUID roleId);
    List<Permission> search(SearchPermissionQuery searchPermissionQuery);
    Long count(SearchPermissionQuery searchPermissionQuery);
}
