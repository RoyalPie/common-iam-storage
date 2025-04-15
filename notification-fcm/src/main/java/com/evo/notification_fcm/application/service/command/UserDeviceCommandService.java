package com.evo.notification_fcm.application.service.command;

import com.evo.notification_fcm.application.dto.request.RegisterOrUpdateDeviceRequest;
import com.evo.notification_fcm.application.dto.request.UnRegisterDeviceRequest;
import com.evo.notification_fcm.application.mapper.CommandMapper;
import com.evo.notification_fcm.domain.UserDevice;
import com.evo.notification_fcm.domain.command.RegisterOrUpdateDeviceCmd;
import com.evo.notification_fcm.domain.repository.TopicSubscriptionDomainRepository;
import com.evo.notification_fcm.domain.repository.UserDeviceDomainRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDeviceCommandService {
    private final UserDeviceDomainRepository userDeviceDomainRepository;
    private final CommandMapper commandMapper;
    private final TopicSubscriptionDomainRepository topicSubscriptionDomainRepository;

    public void registerDevice(RegisterOrUpdateDeviceRequest request) {
        RegisterOrUpdateDeviceCmd registerOrUpdateDeviceCmd = commandMapper.from(request);

        UserDevice existingDeviceRegistration =
                userDeviceDomainRepository.findByDeviceIdAndUserId(request.getDeviceId(), request.getUserId());
        if (existingDeviceRegistration != null) {
            existingDeviceRegistration.setEnabled(true);
            subscribeTokenTopic(registerOrUpdateDeviceCmd.getDeviceToken(), registerOrUpdateDeviceCmd.getUserId());
            userDeviceDomainRepository.save(existingDeviceRegistration);
        } else {
            UserDevice deviceRegistration = new UserDevice(registerOrUpdateDeviceCmd);
            subscribeTokenTopic(registerOrUpdateDeviceCmd.getDeviceToken(), registerOrUpdateDeviceCmd.getUserId());
            userDeviceDomainRepository.save(deviceRegistration);
        }
    }

    public void unregisterDevice(UnRegisterDeviceRequest unRegisterDeviceRequest) {
        UserDevice deviceRegistration = userDeviceDomainRepository.findByDeviceIdAndUserId(
                unRegisterDeviceRequest.getDeviceId(), unRegisterDeviceRequest.getUserId());
        if (deviceRegistration != null) {
            deviceRegistration.setEnabled(false);
            userDeviceDomainRepository.save(deviceRegistration);
            List<String> topics = topicSubscriptionDomainRepository.findTopicEnabled(deviceRegistration.getUserId());
            unsubscribeDeviceFromTopic(unRegisterDeviceRequest, topics);
        }
    }

    public void subscribeTokenTopic(String deviceToken, UUID userId) {
        List<String> fcmTopics = topicSubscriptionDomainRepository.findTopicEnabled(userId);
        fcmTopics.forEach(topic -> {
            try {
                FirebaseMessaging.getInstance(FirebaseApp.getInstance("noti-app"))
                        .subscribeToTopic(List.of(deviceToken), topic);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException("FIREBASE_SUBSCRIBE_TOPIC_FAILED");
            }
        });
    }

    public void unsubscribeDeviceFromTopic(UnRegisterDeviceRequest unRegisterDeviceRequest, List<String> topics) {
        topics.forEach(topic -> {
            try {
                FirebaseMessaging.getInstance(FirebaseApp.getInstance("my-app"))
                        .unsubscribeFromTopic(List.of(unRegisterDeviceRequest.getDeviceToken()), topic);
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException("FIREBASE_SUBSCRIBE_TOPIC_FAILED");
            }
        });
    }
}
