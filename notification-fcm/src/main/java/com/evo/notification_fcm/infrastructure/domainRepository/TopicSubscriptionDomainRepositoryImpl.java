package com.evo.notification_fcm.infrastructure.domainRepository;

import com.evo.common.domainRepository.AbstractDomainRepository;
import com.evo.notification_fcm.domain.TopicSubscription;
import com.evo.notification_fcm.domain.repository.TopicSubscriptionDomainRepository;
import com.evo.notification_fcm.infrastructure.persistence.entity.TopicSubscriptionEntity;
import com.evo.notification_fcm.infrastructure.persistence.mapper.TopicSubscriptionEntityMapper;
import com.evo.notification_fcm.infrastructure.persistence.repository.TopicSubscriptionEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TopicSubscriptionDomainRepositoryImpl extends AbstractDomainRepository<TopicSubscription, TopicSubscriptionEntity, UUID>
        implements TopicSubscriptionDomainRepository {
    private final TopicSubscriptionEntityMapper entityMapper;
    private final TopicSubscriptionEntityRepository repository;

    public TopicSubscriptionDomainRepositoryImpl(TopicSubscriptionEntityMapper entityMapper, TopicSubscriptionEntityRepository repository){
        super(repository, entityMapper);
        this.entityMapper = entityMapper;
        this.repository = repository;
    }

    @Override
    public List<String> findTopicEnabled(UUID userId) {
        return List.of();
    }

    @Override
    public List<TopicSubscription> findByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<UUID> getUserIdsByTopic(String topic) {
        return List.of();
    }

    @Override
    public TopicSubscription getById(UUID userId) {
        TopicSubscriptionEntity topicEntity = repository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("USER_TOPIC_NOT_FOUND"));
        return entityMapper.toDomainModel(topicEntity);
    }
}
