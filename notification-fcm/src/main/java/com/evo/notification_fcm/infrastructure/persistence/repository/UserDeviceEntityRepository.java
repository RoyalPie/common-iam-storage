package com.evo.notification_fcm.infrastructure.persistence.repository;

import com.evo.notification_fcm.infrastructure.persistence.entity.UserDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceEntityRepository extends JpaRepository<UserDeviceEntity, UUID> {

    Optional<UserDeviceEntity> findByDeviceIdAndUserId(UUID deviceId, UUID userId);

    List<UserDeviceEntity> findByDeviceTokenAndEnabledTrue(String deviceToken);

    List<UserDeviceEntity> findByUserIdAndEnabledTrue(UUID userId);

    @Query("SELECT d.deviceToken FROM UserDeviceEntity d WHERE d.userId = :userId AND d.enabled = true")
    List<String> findDeviceTokenByUserId(@Param("userId") UUID userId);

    List<UserDeviceEntity> findByUserIdInAndEnabledTrue(List<UUID> userId);

    @Query("SELECT d FROM UserDeviceEntity d WHERE d.lastLoginAt < :cutOffDate AND d.enabled = false")
    List<UserDeviceEntity> findInactivatedDevices(@Param("cutOffDate") Instant cutOffDate);
}