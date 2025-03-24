package com.evo.ddd.domain;

import com.evo.common.Auditor;
import com.evo.ddd.domain.command.CreateUserRoleCmd;
import com.evo.ddd.infrastructure.support.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class UserRole extends Auditor {
    private UUID id;
    private UUID userId;
    private UUID roleId;

    public UserRole(CreateUserRoleCmd createUserRoleCmd, UUID userId) {
        this.id = IdUtils.newUUID();
        this.userId = userId;
        this.roleId = createUserRoleCmd.getRoleId();
    }
}
