package com.evo.notification_fcm.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;


import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery_record")
public class DeliveryRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "notification_id")
    private UUID notificationId;

    @Column(name = "device_registration_id")
    private UUID deviceRegistrationId;

    @Column(name = "status")
    private String status;

    @CreatedDate
    @Column(name = "send_at")
    private Instant sendAt;

}
