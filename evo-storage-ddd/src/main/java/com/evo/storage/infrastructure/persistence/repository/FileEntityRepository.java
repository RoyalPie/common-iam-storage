package com.evo.storage.infrastructure.persistence.repository;

import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import com.evo.storage.infrastructure.persistence.repository.custom.FileEntityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileEntityRepository extends JpaRepository<FileEntity, UUID>, FileEntityRepositoryCustom {
}
