package com.evo.notification_fcm.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import com.evo.notification_fcm.domain.UserDevice;
import com.evo.notification_fcm.infrastructure.persistence.entity.UserDeviceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDeviceEntityMapper extends EntityMapper<UserDevice, UserDeviceEntity> {
}
