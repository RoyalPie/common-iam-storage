package com.evo.ddd.infrastructure.persistence.mapper;


import com.evo.common.mapper.EntityMapper;
import com.evo.ddd.domain.UserRole;
import com.evo.ddd.infrastructure.persistence.entity.UserRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleEntityMapper extends EntityMapper<UserRole, UserRoleEntity> {
}
