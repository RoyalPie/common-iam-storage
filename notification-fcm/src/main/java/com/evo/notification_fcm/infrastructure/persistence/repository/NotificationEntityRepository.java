package com.evo.notification_fcm.infrastructure.persistence.repository;

import com.evo.notification_fcm.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, UUID> {}
