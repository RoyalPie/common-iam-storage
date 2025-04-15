package com.evo.notification_fcm.domain.repository;

import com.evo.common.domainRepository.DomainRepository;
import com.evo.notification_fcm.domain.UserDevice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserDeviceDomainRepository extends DomainRepository<UserDevice, UUID> {
    List<UserDevice> findByUserIdAndEnabled(UUID userId);

    List<UserDevice> findByDeviceTokenAndEnabled(String deviceToken);

    UserDevice findByDeviceIdAndUserId(UUID deviceId, UUID userId);

    List<String> getDeviceTokensByUserId(UUID userId);

    List<UserDevice> findByUserIdInAndEnabledTrue(List<UUID> userIds);

    List<UserDevice> findInactivatedDevices(Instant cutoffDate);

    void hardDeleteDeviceRegistration(List<UserDevice> deviceRegistrations);
}
