package com.evo.notification_fcm.infrastructure.persistence.repository;

import com.evo.notification_fcm.infrastructure.persistence.entity.DeliveryRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRecordEntityRepository extends JpaRepository<DeliveryRecordEntity, UUID> {
    List<DeliveryRecordEntity> findByDeviceRegistrationIdIn(List<UUID> deviceIds);
}