package com.evo.ddd.domain;

import com.evo.common.Auditor;
import com.evo.ddd.domain.command.CreateRolePermissionCmd;
import com.evo.ddd.infrastructure.support.IdUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class RolePermission extends Auditor {
    private UUID id;
    private UUID roleId;
    private UUID permissionId;
    private boolean deleted;

    public RolePermission(CreateRolePermissionCmd cmd) {
        this.id = IdUtils.newUUID();
        this.roleId = cmd.getRoleId();
        this.permissionId = cmd.getPermissionId();
        this.deleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RolePermission that = (RolePermission) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId, permissionId);
    }
}
