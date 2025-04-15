package com.evo.notification_fcm.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import com.evo.notification_fcm.domain.DeliveryRecord;
import com.evo.notification_fcm.infrastructure.persistence.entity.DeliveryRecordEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryEntityMapper extends EntityMapper<DeliveryRecord, DeliveryRecordEntity> {
}
