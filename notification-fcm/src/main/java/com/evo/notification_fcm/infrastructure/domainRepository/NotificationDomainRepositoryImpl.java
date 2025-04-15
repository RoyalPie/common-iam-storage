package com.evo.notification_fcm.infrastructure.domainRepository;

import com.evo.common.domainRepository.AbstractDomainRepository;
import com.evo.notification_fcm.domain.Notification;
import com.evo.notification_fcm.domain.repository.NotificationDomainRepository;
import com.evo.notification_fcm.infrastructure.persistence.entity.NotificationEntity;
import com.evo.notification_fcm.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.evo.notification_fcm.infrastructure.persistence.repository.NotificationEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class NotificationDomainRepositoryImpl extends AbstractDomainRepository<Notification, NotificationEntity, UUID>
        implements NotificationDomainRepository {
    private final NotificationEntityMapper entityMapper;
    private final NotificationEntityRepository repository;
    public NotificationDomainRepositoryImpl(NotificationEntityMapper entityMapper, NotificationEntityRepository repository){
        super(repository, entityMapper);
        this.entityMapper = entityMapper;
        this.repository = repository;
    }
    @Override
    public Notification getById(UUID uuid) {
        return null;
    }
}
