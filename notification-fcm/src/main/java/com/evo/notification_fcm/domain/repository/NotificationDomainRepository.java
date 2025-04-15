package com.evo.notification_fcm.domain.repository;

import com.evo.common.domainRepository.DomainRepository;
import com.evo.notification_fcm.domain.Notification;

import java.util.UUID;

public interface NotificationDomainRepository extends DomainRepository<Notification, UUID> {
}
