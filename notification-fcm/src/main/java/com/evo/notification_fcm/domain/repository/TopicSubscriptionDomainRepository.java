package com.evo.notification_fcm.domain.repository;

import com.evo.common.domainRepository.DomainRepository;
import com.evo.notification_fcm.domain.TopicSubscription;

import java.util.List;
import java.util.UUID;

public interface TopicSubscriptionDomainRepository extends DomainRepository<TopicSubscription, UUID> {
    List<String> findTopicEnabled(UUID userId);

    List<TopicSubscription> findByUserId(UUID userId);

    List<UUID> getUserIdsByTopic(String topic);
}
