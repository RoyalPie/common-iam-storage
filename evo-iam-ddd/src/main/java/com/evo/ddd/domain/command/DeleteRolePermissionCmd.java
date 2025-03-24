package com.evo.ddd.domain.command;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeleteRolePermissionCmd {
    private UUID roleId;
    private UUID permissionId;
}
