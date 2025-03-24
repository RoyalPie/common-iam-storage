package com.evo.ddd.infrastructure.persistence.repository;

import com.evo.ddd.infrastructure.persistence.entity.PermissionEntity;
import com.evo.ddd.infrastructure.persistence.repository.custom.PermissionEntityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PermissionEntityRepository extends JpaRepository<PermissionEntity, UUID>, PermissionEntityRepositoryCustom {
    PermissionEntity findByResourceAndScope(String resource, String scope);
    List<PermissionEntity> findByIdIn(List<UUID> ids);
    @Query("SELECT p " +
            "FROM RoleEntity r " +
            "LEFT JOIN RolePermissionEntity rp ON r.id = rp.roleId " +
            "LEFT JOIN PermissionEntity p ON rp.permissionId = p.id " +
            "WHERE r.id = :roleId")
    List<PermissionEntity> findPermissionByRoleId(@Param("roleId") UUID roleId);
}
