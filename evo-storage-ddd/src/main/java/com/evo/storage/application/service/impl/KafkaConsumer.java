package com.evo.storage.application.service.impl;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = "avatar-upload", groupId = "iam-group")
    public void consumeMessage(String message) {
        System.out.println("Received message in Storage Service: " + message);
    }
    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        System.out.println("✅ Kafka Consumer is listening...");
    }
}
