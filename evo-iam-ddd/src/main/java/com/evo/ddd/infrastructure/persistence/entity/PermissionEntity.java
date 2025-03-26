package com.evo.ddd.infrastructure.persistence.entity;

import com.evo.common.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "permissions")
public class PermissionEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "resource", nullable = false, length = 50)
    private String resource;

    @Column(name = "scope", nullable = false, length = 50)
    private String scope;

    @Column(name = "deleted")
    private boolean deleted = false;
}
