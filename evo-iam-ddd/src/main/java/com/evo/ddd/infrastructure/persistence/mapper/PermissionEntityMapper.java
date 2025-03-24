package com.evo.ddd.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.infrastructure.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionEntityMapper extends EntityMapper<Permission, PermissionEntity> {
}
