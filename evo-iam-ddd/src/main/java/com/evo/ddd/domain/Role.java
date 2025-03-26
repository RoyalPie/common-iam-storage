package com.evo.ddd.domain;

import com.evo.common.Auditor;
import com.evo.ddd.domain.command.CreateOrUpdateRoleCmd;
import com.evo.ddd.domain.command.CreateRolePermissionCmd;
import com.evo.ddd.infrastructure.support.IdUtils;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Role extends Auditor {
    private UUID id;
    private String name;
    private boolean isRoot;
    private boolean deleted;
    private List<RolePermission> rolePermissions;

    public Role(CreateOrUpdateRoleCmd cmd) {
        if (cmd.getId() != null) {
            this.id = cmd.getId();
        } else {
            this.id = IdUtils.newUUID();
        }
        this.name = cmd.getName();
        this.deleted= cmd.isDeleted();
        this.isRoot = cmd.isRoot();

        this.rolePermissions = new ArrayList<>();
        if (cmd.getRolePermission() != null) {
            cmd.getRolePermission().forEach(createRolePermissionCmd -> {
                createRolePermissionCmd.setRoleId(this.id);
                rolePermissions.add(new RolePermission(createRolePermissionCmd));
            });
        }
    }

    public void update(CreateOrUpdateRoleCmd cmd) {
        this.name = cmd.getName();
        this.deleted = cmd.isDeleted();
        this.isRoot = cmd.isRoot();
//tách riêng  ra 1 hàm phục vụ cho việc update v tạo mới
        if (this.rolePermissions == null) {
            this.rolePermissions = new ArrayList<>();
        }

        // Tạo map chứa rolePermission hiện tại và tạm thời xoá mềm
        Map<UUID, RolePermission> existingPermissionsMap = this.rolePermissions.stream()
                .peek(rp -> rp.setDeleted(true))
                .collect(Collectors.toMap(RolePermission::getPermissionId, rp -> rp));

        for (CreateRolePermissionCmd rolePermissionCmd : cmd.getRolePermission()) {
            UUID permissionId = rolePermissionCmd.getPermissionId();
            if (existingPermissionsMap.containsKey(permissionId)) {
                // Nếu đã tồn tại, cập nhật deleted = false
                existingPermissionsMap.get(permissionId).setDeleted(false);
            } else {
                // Nếu chưa tồn tại, tạo mới RolePermission
                rolePermissionCmd.setRoleId(this.id);
                RolePermission newRolePermission = new RolePermission(rolePermissionCmd);
                this.rolePermissions.add(newRolePermission);
            }
        }
    }
}
