package com.evo.elasticsearch.application.service.impl;

import com.evo.elasticsearch.application.service.UserCommandService;
import com.evo.elasticsearch.domain.User;
import com.evo.elasticsearch.domain.command.SyncUserCmd;
import com.evo.elasticsearch.domain.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
    private final UserDomainRepository userDomainRepository;
    @Override
    public void create(SyncUserCmd cmd) {
        User user = new User(cmd);
        userDomainRepository.save(user);
    }

    @Override
    public void update(SyncUserCmd cmd) {
        User user = userDomainRepository.getById(cmd.getUserID());
        user.update(cmd);
        userDomainRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        userDomainRepository.deleteById(userId);
    }
}
