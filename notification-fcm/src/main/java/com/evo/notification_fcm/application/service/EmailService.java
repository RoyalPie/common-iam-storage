//package com.evo.notification_fcm.application.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//    private final ThymeleafEmailService templateService;
//
//    @Value("${spring.mail.username}")
//    private String sender;
//
//    public void sendTemplateEmail(
//            String to, String subject, String templateName, Map<String, Object> templateVariables) {
//        try {
//            // Xử lý template với Thymeleaf
//            String htmlContent = templateService.processTemplate(templateName, templateVariables);
//
//            // Tạo và gửi email
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//            helper.setFrom(sender);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(htmlContent, true); // true = isHtml
//
//            mailSender.send(mimeMessage);
//            log.info("Template email '{}' đã gửi thành công đến: {}", templateName, to);
//        } catch (MessagingException e) {
//            log.error("Lỗi gửi template email '{}' đến {}: {}", templateName, to, e.getMessage());
//            throw new AppException(AppErrorCode.CANT_SEND_EMAIL);
//        }
//    }
//}