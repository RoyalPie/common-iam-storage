package com.evo.ddd.application.mapper;

import com.evo.common.dto.request.SyncUserRequest;
import com.evo.ddd.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SyncMapper {
    SyncUserRequest from(User user);
}