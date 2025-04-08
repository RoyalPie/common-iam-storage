package com.evo.elasticsearch.application.service;

import com.evo.common.dto.request.SyncUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMessageConsumeService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "sync-user")
    public void syncUser(SyncUserRequest request){
        System.out.println("Sync this user: " + request.getUsername() +"  " + request.getEmail());
    }
}
