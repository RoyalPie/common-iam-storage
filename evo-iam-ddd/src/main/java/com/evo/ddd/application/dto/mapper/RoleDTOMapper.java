package com.evo.ddd.application.dto.mapper;

import com.evo.common.mapper.DTOMapper;
import com.evo.ddd.application.dto.response.RoleDTO;
import com.evo.ddd.domain.Role;
import com.evo.ddd.infrastructure.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleDTOMapper extends DTOMapper<RoleDTO, Role, RoleEntity> {

}
