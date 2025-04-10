package com.evo.elasticsearch.application.service;

import com.evo.common.dto.event.SyncUserEvent;
import com.evo.common.dto.request.SyncUserRequest;
import com.evo.elasticsearch.application.mapper.CommandMapper;
import com.evo.elasticsearch.domain.command.SyncUserCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMessageConsumeService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserCommandService userCommandService;
    private final CommandMapper commandMapper;

    @KafkaListener(topics = "sync-user")
    public void syncUser(SyncUserEvent syncUserEvent){
        SyncUserCmd cmd = commandMapper.from(syncUserEvent.getSyncUserRequest());
        String actionType = syncUserEvent.getSyncAction();
        switch (actionType) {
            case "CREATE_USER":
                userCommandService.create(cmd);
                break;
            case "UPDATE_USER":
                userCommandService.update(cmd);
                break;
            case "DELETE_USER":
                userCommandService.delete(cmd.getUserID());
                break;
            default:
                log.error("Invalid sync user action: {}", syncUserEvent.getSyncAction());
        }

    }
}
