package com.evo.ddd.application.dto.mapper;

import com.evo.ddd.application.dto.response.PermissionDTO;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.infrastructure.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionDTOMapper extends DTOMapper<PermissionDTO, Permission, PermissionEntity> {

}
