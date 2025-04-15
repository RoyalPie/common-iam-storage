package com.evo.notification_fcm.application.service.command;

import com.evo.common.dto.event.PushNotificationEvent;
import com.evo.notification_fcm.application.mapper.CommandMapper;
import com.evo.notification_fcm.domain.Notification;
import com.evo.notification_fcm.domain.command.StoredNotificationCmd;
import com.evo.notification_fcm.domain.repository.NotificationDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {
    private final NotificationDomainRepository notificationDomainRepository;
    private final CommandMapper commandMapper;

    public Notification storeNotification(PushNotificationEvent request) {
        StoredNotificationCmd storeNotificationCmd = commandMapper.from(request);
        Notification notification = new Notification(storeNotificationCmd);
        return notificationDomainRepository.save(notification);
    }
}