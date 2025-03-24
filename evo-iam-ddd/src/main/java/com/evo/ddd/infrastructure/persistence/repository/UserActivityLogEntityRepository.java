package com.evo.ddd.infrastructure.persistence.repository;

import com.evo.ddd.infrastructure.persistence.entity.UserActivityLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserActivityLogEntityRepository extends JpaRepository<UserActivityLogEntity, UUID> {
}
