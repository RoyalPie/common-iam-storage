package com.evo.iam.service;


import com.evo.iam.entity.Permission;
import com.evo.iam.entity.Role;
import com.evo.iam.repository.PermissionRepository;
import com.evo.iam.repository.RoleRepository;
import com.evo.iam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemAdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public SystemAdminService(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Page<Role> getAllRoles(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return roleRepository.findAll(pageable);
    }

    public Page<Permission> getAllPermissions(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return permissionRepository.findAll(pageable);
    }

    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new RuntimeException("Role already exists");
        }
        return roleRepository.save(role);
    }

    public void deleteRole(String roleName) {
        roleRepository.findByName(roleName)
                .ifPresentOrElse(role -> {
                    role.setDeleted(true);
                    roleRepository.save(role);
                }, () -> {
                    throw new EntityNotFoundException("Role not found: " + roleName);
                });
    }


    public void createPermission(String resource, String action) {
        String fullName = resource + "." + action;

        if (permissionRepository.findByName(fullName).isPresent()) {
            throw new RuntimeException("Permission already exists");
        }

        Permission permission = new Permission();
        permission.setResource(resource.toUpperCase());
        permission.setPermission(action.toUpperCase());
        permissionRepository.save(permission);
    }

    public void addPermissionToRole(String roleName, String permissionName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        role.getPermissions().add(permission);
        roleRepository.save(role);
    }

    public void removePermissionFromRole(String roleName, String permissionName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }

    public void deletePermission(String permissionName) {
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            role.getPermissions().remove(permission);
        }
        roleRepository.saveAll(roles);

        permission.setDeleted(true);
        permissionRepository.save(permission);
    }

}
