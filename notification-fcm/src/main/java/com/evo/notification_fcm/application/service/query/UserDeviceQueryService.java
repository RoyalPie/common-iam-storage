package com.evo.notification_fcm.application.service.query;

import com.evo.notification_fcm.domain.UserDevice;
import com.evo.notification_fcm.domain.repository.UserDeviceDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDeviceQueryService {
    private final UserDeviceDomainRepository userDeviceDomainRepository;

    public List<UserDevice> getDeviceByUserIdAndEnable(UUID token) {
        return userDeviceDomainRepository.findByUserIdAndEnabled(token);
    }

    public List<UserDevice> getDevicesByListUserId(List<UUID> userIds) {
        return userDeviceDomainRepository.findByUserIdInAndEnabledTrue(userIds);
    }
}