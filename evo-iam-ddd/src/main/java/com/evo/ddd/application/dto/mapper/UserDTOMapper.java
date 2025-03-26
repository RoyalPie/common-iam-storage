package com.evo.ddd.application.dto.mapper;

import com.evo.ddd.application.dto.response.UserDTO;
import com.evo.ddd.domain.User;
import com.evo.ddd.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper extends DTOMapper<UserDTO, User, UserEntity> {
}
