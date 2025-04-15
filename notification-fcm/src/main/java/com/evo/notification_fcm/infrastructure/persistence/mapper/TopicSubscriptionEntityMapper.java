package com.evo.notification_fcm.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import com.evo.notification_fcm.domain.TopicSubscription;
import com.evo.notification_fcm.infrastructure.persistence.entity.TopicSubscriptionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicSubscriptionEntityMapper extends EntityMapper<TopicSubscription, TopicSubscriptionEntity> {
}
