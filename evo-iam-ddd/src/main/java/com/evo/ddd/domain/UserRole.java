package com.evo.ddd.domain;

import com.evo.common.Auditor;
import com.evo.ddd.domain.command.CreateUserRoleCmd;
import com.evo.ddd.infrastructure.support.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class UserRole extends Auditor {
    private UUID id;
    private UUID userId;
    private UUID roleId;

    public UserRole(CreateUserRoleCmd cmd) {
        this.id = IdUtils.newUUID();
        this.userId = cmd.getUserId();
        this.roleId = cmd.getRoleId();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserRole that = (UserRole) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
