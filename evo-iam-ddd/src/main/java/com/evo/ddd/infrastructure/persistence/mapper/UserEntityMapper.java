package com.evo.ddd.infrastructure.persistence.mapper;


import com.evo.common.mapper.EntityMapper;
import com.evo.ddd.domain.User;
import com.evo.ddd.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper extends EntityMapper<User, UserEntity> {
}
