package com.evo.ddd.infrastructure.persistence.repository;

import com.evo.ddd.infrastructure.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleEntityRepository extends JpaRepository<UserRoleEntity, UUID> {
    List<UserRoleEntity> findByUserIdIn(List<UUID> userIds);
}
