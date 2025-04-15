package com.evo.notification_fcm.application.service.command;

import com.evo.common.dto.request.UpdateTopicsOfUserRequest;
import com.evo.common.enums.FCMTopic;
import com.evo.notification_fcm.domain.TopicSubscription;
import com.evo.notification_fcm.domain.command.CreateTopicCmd;
import com.evo.notification_fcm.domain.repository.TopicSubscriptionDomainRepository;
import com.evo.notification_fcm.domain.repository.UserDeviceDomainRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTopicCommandService {
    private final TopicSubscriptionDomainRepository topicSubscriptionDomainRepository;
    private final UserDeviceDomainRepository userDeviceDomainRepository;

    public void initUserTopic(UUID userId) {
        List<String> topics = FCMTopic.getAllTopics();
        List<TopicSubscription> userTopics = topics.stream()
                .map(topic -> {
                    CreateTopicCmd createUserTopicCmd = CreateTopicCmd.builder()
                            .userId(userId)
                            .topic(topic)
                            .enabled(true)
                            .build();
                    return new TopicSubscription(createUserTopicCmd);
                })
                .toList();
        topicSubscriptionDomainRepository.saveAll(userTopics);
    }

    public void updateTopicOfUser(UpdateTopicsOfUserRequest updateTopicsOfUserRequest) {
        List<TopicSubscription> existingUserTopics =
                topicSubscriptionDomainRepository.findByUserId(updateTopicsOfUserRequest.getUserId());
        Map<String, TopicSubscription> existingUserTopicMap = existingUserTopics.stream()
                .map(ut -> {
                    ut.setEnabled(false);
                    return ut;
                })
                .collect(Collectors.toMap(TopicSubscription::getTopic, ut -> ut));

        updateTopicsOfUserRequest.getTopics().forEach(topic -> {
            if (existingUserTopicMap.containsKey(topic)) {
                existingUserTopicMap.get(topic).setEnabled(true);
            } else {
                CreateTopicCmd createUserTopicCmd = CreateTopicCmd.builder()
                        .userId(updateTopicsOfUserRequest.getUserId())
                        .topic(topic)
                        .enabled(true)
                        .build();
                TopicSubscription userTopic = new TopicSubscription(createUserTopicCmd);
                existingUserTopics.add(userTopic);
            }
        });
        topicSubscriptionDomainRepository.saveAll(existingUserTopics);

        List<String> deviceTokens =
                userDeviceDomainRepository.getDeviceTokensByUserId(updateTopicsOfUserRequest.getUserId());

        existingUserTopics.forEach(userTopic -> {
            try {
                if (userTopic.isEnabled()) {
                    FirebaseMessaging.getInstance(FirebaseApp.getInstance("noti-app"))
                            .subscribeToTopic(deviceTokens, userTopic.getTopic());
                } else {
                    FirebaseMessaging.getInstance(FirebaseApp.getInstance("noti-app"))
                            .unsubscribeFromTopic(deviceTokens, userTopic.getTopic());
                }
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException("FIREBASE_SUBSCRIBE_TOPIC_FAILED");
            }
        });
    }
}
