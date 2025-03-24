package com.evo.ddd.infrastructure.persistence.mapper;


import com.evo.common.mapper.EntityMapper;
import com.evo.ddd.domain.UserActivityLog;
import com.evo.ddd.infrastructure.persistence.entity.UserActivityLogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserActivityLogEntityMapper extends EntityMapper<UserActivityLog, UserActivityLogEntity> {
}
