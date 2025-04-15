package com.evo.notification_fcm.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import com.evo.notification_fcm.domain.Notification;
import com.evo.notification_fcm.infrastructure.persistence.entity.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationEntityMapper extends EntityMapper<Notification, NotificationEntity> {
}
