package com.evo.ddd.infrastructure.persistence.repository.custom;

import com.evo.ddd.domain.query.SearchPermissionQuery;
import com.evo.ddd.infrastructure.persistence.entity.PermissionEntity;

import java.util.List;

public interface PermissionEntityRepositoryCustom {
    List<PermissionEntity> search(SearchPermissionQuery searchPermissionQuery);
    Long count(SearchPermissionQuery searchPermissionQuery);
}
