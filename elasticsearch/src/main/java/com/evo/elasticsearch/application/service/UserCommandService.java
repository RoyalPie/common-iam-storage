package com.evo.elasticsearch.application.service;

import com.evo.common.dto.request.SyncUserRequest;
import com.evo.elasticsearch.domain.command.SyncUserCmd;

import java.util.UUID;

public interface UserCommandService {
    void create(SyncUserCmd cmd);
    void update(SyncUserCmd cmd);
    void delete(UUID userId);
}
