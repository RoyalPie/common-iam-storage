package com.evo.ddd.infrastructure.persistence.mapper;


import com.evo.common.mapper.EntityMapper;
import com.evo.ddd.domain.Role;
import com.evo.ddd.infrastructure.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper extends EntityMapper<Role, RoleEntity> {
    void updateEntity(Role domainModel, @MappingTarget RoleEntity roleEntity);
}
