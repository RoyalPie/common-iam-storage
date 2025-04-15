package com.evo.notification_fcm.application.service.command;

import com.evo.common.dto.event.PushNotificationEvent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseNotificationService {
    public void sendNotificationToToken(PushNotificationEvent pushNotificationEvent) {
        try {
            Message message = buildNotificationMessage(pushNotificationEvent);
            sendAndGetResponse(message);
        } catch (Exception e) {
            throw new RuntimeException("FIREBASE_SEND_NOTIFICATION_FAILED");
        }
    }

    public void sendNotificationToTopic(PushNotificationEvent pushNotificationEvent) {
        try {
            Message message = buildNotificationMessageForTopic(pushNotificationEvent);
            sendAndGetResponse(message);
        } catch (Exception e) {
            throw new RuntimeException("FIREBASE_SEND_NOTIFICATION_FAILED");
        }
    }

    private Message buildNotificationMessage(PushNotificationEvent pushNotificationEvent) {

        return Message.builder()
                .setToken(pushNotificationEvent.getToken())
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(pushNotificationEvent.getTitle())
                        .setBody(pushNotificationEvent.getBody())
                        .build())
                .putAllData(pushNotificationEvent.getData() != null ? pushNotificationEvent.getData() : Map.of())
                .build();
    }

    private Message buildNotificationMessageForTopic(PushNotificationEvent pushNotificationEvent) {
        return Message.builder()
                .setTopic(pushNotificationEvent.getTopic())
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(pushNotificationEvent.getTitle())
                        .setBody(pushNotificationEvent.getBody())
                        .build())
                .putAllData(pushNotificationEvent.getData() != null ? pushNotificationEvent.getData() : Map.of())
                .build();
    }

    @Retryable(
            retryFor = {FirebaseMessagingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    private String sendAndGetResponse(Message message) throws ExecutionException, InterruptedException {
        return FirebaseMessaging.getInstance(FirebaseApp.getInstance("noti-app"))
                .sendAsync(message)
                .get();
    }
}