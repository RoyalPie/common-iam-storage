package com.evo.ddd.infrastructure.persistence.entity;

import com.evo.common.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "roles")
public class RoleEntity extends AuditEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "is_root")
    private Boolean isRoot = false;

    @Column(name = "deleted")
    private boolean deleted = false;
}

