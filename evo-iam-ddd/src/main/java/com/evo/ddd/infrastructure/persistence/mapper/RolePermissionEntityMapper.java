package com.evo.ddd.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import com.evo.ddd.domain.RolePermission;
import com.evo.ddd.infrastructure.persistence.entity.RolePermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolePermissionEntityMapper extends EntityMapper<RolePermission, RolePermissionEntity> {
}
