package com.evo.notification_fcm.infrastructure.domainRepository;

import com.evo.common.domainRepository.AbstractDomainRepository;
import com.evo.notification_fcm.domain.UserDevice;
import com.evo.notification_fcm.domain.repository.UserDeviceDomainRepository;
import com.evo.notification_fcm.infrastructure.persistence.entity.UserDeviceEntity;
import com.evo.notification_fcm.infrastructure.persistence.mapper.UserDeviceEntityMapper;
import com.evo.notification_fcm.infrastructure.persistence.repository.UserDeviceEntityRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDeviceDomainRepositoryImpl extends AbstractDomainRepository<UserDevice, UserDeviceEntity, UUID>
        implements UserDeviceDomainRepository {
    private final UserDeviceEntityMapper entityMapper;
    private final UserDeviceEntityRepository repository;

    public UserDeviceDomainRepositoryImpl(UserDeviceEntityMapper entityMapper, UserDeviceEntityRepository repository){
        super(repository, entityMapper);
        this.entityMapper = entityMapper;
        this.repository = repository;
    }

    @Override
    public List<UserDevice> findByUserIdAndEnabled(UUID userId) {

        return List.of();
    }

    @Override
    public List<UserDevice> findByDeviceTokenAndEnabled(String deviceToken) {
        return List.of();
    }

    @Override
    public UserDevice findByDeviceIdAndUserId(UUID deviceId, UUID userId) {
        return null;
    }

    @Override
    public List<String> getDeviceTokensByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<UserDevice> findByUserIdInAndEnabledTrue(List<UUID> userIds) {
        return List.of();
    }

    @Override
    public List<UserDevice> findInactivatedDevices(Instant cutoffDate) {
        return List.of();
    }

    @Override
    public void hardDeleteDeviceRegistration(List<UserDevice> deviceRegistrations) {

    }

    @Override
    public UserDevice getById(UUID uuid) {
        return null;
    }
}
