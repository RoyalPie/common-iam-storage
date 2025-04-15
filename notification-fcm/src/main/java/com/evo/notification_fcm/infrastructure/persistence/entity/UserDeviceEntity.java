package com.evo.notification_fcm.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_device", uniqueConstraints = @UniqueConstraint(columnNames = {"device_id", "user_id"}))
public class UserDeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "device_id")
    private UUID deviceId;

    @LastModifiedDate
    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "enabled")
    private boolean enabled;
}
