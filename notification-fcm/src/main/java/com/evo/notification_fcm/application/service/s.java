//package com.evo.notification_fcm.application.service;
//
//import com.evo.common.dto.event.PushNotificationEvent;
//import com.evo.common.dto.event.SendNotificationEvent;
//import com.evo.common.enums.Channel;
//import com.evo.notification_fcm.application.service.command.FirebaseNotificationService;
//import com.evo.notification_fcm.application.service.command.NotificationCommandService;
//import com.evo.notification_fcm.application.service.query.UserDeviceQueryService;
//import com.evo.notification_fcm.domain.Notification;
//import com.evo.notification_fcm.domain.UserDevice;
//import com.evo.notification_fcm.domain.command.StoreDeliveryRecordCmd;
//import com.evo.notification_fcm.domain.repository.TopicSubscriptionDomainRepository;
//import com.evo.notification_fcm.domain.repository.UserDeviceDomainRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class NotificationConsumerService {
//    private final TemplateCodeMapper templateCodeMapper;
//    private final NotificationCommandService notificationCommandService;
//    private final UserDeviceQueryService userDeviceQueryService;
//    private final FirebaseNotificationService firebaseNotificationService;
//    private final UserDeviceDomainRepository userDeviceDomainRepository;
//    private final TopicSubscriptionDomainRepository topicSubscriptionDomainRepository;
//
//    @KafkaListener(topics = "send-notification-group")
//    public void handleSendNotification(SendNotificationEvent event) {
//        try {
//            if (Channel.EMAIL.name().equals(event.getChannel())) {
//                processEmailNotification(event);
//            } else if (Channel.SMS.name().equals(event.getChannel())) {
//                // Gửi tin nhắn SMS
//            } else if (Channel.PUSH_NOTIFICATION.name().equals(event.getChannel())) {
//                // Gửi thông báo push
//            }
//        } catch (Exception e) {
//            log.error("Lỗi xử lý thông báo: {}", e.getMessage(), e);
//        }
//    }
//
//    @KafkaListener(topics = "push-notification-group")
//    public void handlePushNotification(PushNotificationEvent event) {
//        try {
//            if (event.getTopic() != null) {
//                firebaseNotificationService.sendNotificationToTopic(event);
//                Notification notification = notificationCommandService.storeNotification(event);
//                List<UUID> userIds = topicSubscriptionDomainRepository.getUserIdsByTopic(event.getTopic());
//                List<UserDevice> deviceRegistrations =
//                        userDeviceQueryService.getDevicesByListUserId(userIds);
//                deviceRegistrations.forEach(deviceRegistration -> {
//                    StoreDeliveryRecordCmd storeNotificationDeliveryCmd = StoreDeliveryRecordCmd.builder()
//                            .notificationId(notification.getId())
//                            .deviceRegistrationId(deviceRegistration.getId())
//                            .status("SENT")
//                            .sendAt(Instant.ofEpochSecond(System.currentTimeMillis()))
//                            .build();
//                    deviceRegistration.addNotificationDelivery(storeNotificationDeliveryCmd);
//                    userDeviceDomainRepository.save(deviceRegistration);
//                });
//            } else {
//                Notification notification = notificationCommandService.storeNotification(event);
//                pushNotificationToUser(event, notification.getId());
//            }
//        } catch (Exception e) {
//            log.error("Lỗi xử lý thông báo: {}", e.getMessage(), e);
//        }
//    }
//
//    private void processEmailNotification(SendNotificationEvent event) {
//        // Map templateCode từ event sang template name của Thymeleaf
//        String templateName = templateCodeMapper.mapToTemplateName(event.getTemplateCode());
//
//        // Lấy subject dựa vào templateCode
//        String subject = templateCodeMapper.getSubject(event.getTemplateCode());
//
//        // Gửi email với template Thymeleaf
//        emailService.sendTemplateEmail(event.getRecipient(), subject, templateName, event.getParam());
//    }
//
//    private void pushNotificationToUser(PushNotificationEvent event, UUID notificationId) {
//        List<UserDevice> deviceRegistrations =
//                userDeviceQueryService.getDeviceByUserIdAndEnable(event.getUserId());
//        deviceRegistrations.forEach(deviceRegistration -> {
//            event.setToken(deviceRegistration.getDeviceToken());
//            firebaseNotificationService.sendNotificationToToken(event);
//            StoreDeliveryRecordCmd storeNotificationDeliveryCmd = StoreDeliveryRecordCmd.builder()
//                    .notificationId(notificationId)
//                    .deviceRegistrationId(deviceRegistration.getId())
//                    .status("SENT")
//                    .sendAt(Instant.ofEpochSecond(System.currentTimeMillis()))
//                    .build();
//            deviceRegistration.addNotificationDelivery(storeNotificationDeliveryCmd);
//            userDeviceDomainRepository.save(deviceRegistration);
//        });
//    }
//}
