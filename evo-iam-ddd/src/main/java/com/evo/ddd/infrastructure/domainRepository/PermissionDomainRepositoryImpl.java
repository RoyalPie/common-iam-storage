package com.evo.ddd.infrastructure.domainRepository;

import com.evo.common.domainRepository.AbstractDomainRepository;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.query.SearchPermissionQuery;
import com.evo.ddd.domain.repository.PermissionDomainRepository;
import com.evo.ddd.infrastructure.persistence.entity.PermissionEntity;
import com.evo.ddd.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.evo.ddd.infrastructure.persistence.repository.PermissionEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PermissionDomainRepositoryImpl extends AbstractDomainRepository<Permission, PermissionEntity, UUID>
        implements PermissionDomainRepository {
    private final PermissionEntityMapper entityMapper;
    private final PermissionEntityRepository repository;

    public PermissionDomainRepositoryImpl(PermissionEntityRepository repository, PermissionEntityMapper entityMapper) {
        super(repository, entityMapper);
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<Permission> findPermissionByRoleId(UUID roleId) {
        List<PermissionEntity> permissionEntities = repository.findPermissionByRoleId(roleId);
        return entityMapper.toDomainModelList(permissionEntities);
    }

    @Override
    public List<Permission> search(SearchPermissionQuery searchPermissionQuery) {
        List<PermissionEntity> permissionEntities = repository.search(searchPermissionQuery);
        return permissionEntities.stream().map(entityMapper::toDomainModel).toList();
    }

    @Override
    public Long count(SearchPermissionQuery searchPermissionQuery) {
        return repository.count(searchPermissionQuery);
    }

    @Override
    public Permission getById(UUID uuid) {
        return entityMapper.toDomainModel(repository.findById(uuid).orElseThrow(() -> new RuntimeException("Permission not found")));
    }
}