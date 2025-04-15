package com.evo.notification_fcm.infrastructure.persistence.repository;

import com.evo.notification_fcm.infrastructure.persistence.entity.TopicSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TopicSubscriptionEntityRepository extends JpaRepository<TopicSubscriptionEntity, UUID> {

    @Query("SELECT t.topic FROM TopicSubscriptionEntity t WHERE :userId = t.userId AND t.enabled = true")
    List<String> findTopicEnabled(@Param("userId") UUID userId);

    List<TopicSubscriptionEntity> findByUserId(UUID userId);

    @Query("SELECT t.userId FROM TopicSubscriptionEntity t WHERE t.topic = :topic AND t.enabled = true")
    List<UUID> findUserIdByTopic(@Param("topic") String topic);
}
