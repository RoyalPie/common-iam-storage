package com.evo.iam.repository;

import com.evo.iam.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("SELECT DISTINCT CONCAT(p.resource, '.', p.permission) FROM User u " +
            "JOIN u.roles r " +
            "JOIN r.permissions p " +
            "WHERE u.email = :email AND u.deleted = false AND r.deleted = false AND p.deleted = false")
    Set<String> findUserPermissions(@Param("email") String email);

    @Query("SELECT p FROM Permission p WHERE CONCAT(p.resource, '.', p.permission) = :name AND p.deleted = false")
    Optional<Permission> findByName(@Param("name") String name);

    @Query("SELECT p FROM Permission p WHERE p.deleted = false")
    Page<Permission> findAll(Pageable pageable);

}