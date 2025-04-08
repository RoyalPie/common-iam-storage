package com.evo.ddd.presentation.rest;

import com.evo.common.dto.request.SyncUserRequest;
import com.evo.ddd.application.mapper.SyncMapper;
import com.evo.ddd.domain.User;
import com.evo.ddd.domain.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserDomainRepository userDomainRepository;
    private final SyncMapper syncMapper;

    @PostMapping("/test")
    public void test(Authentication authentication) {
        User user = userDomainRepository.getByUsername(authentication.getName());
        SyncUserRequest request = syncMapper.from(user);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("sync-user", request);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + result +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        request + "] due to : " + ex.getMessage());
            }
        });
    }
}
