package com.evo.ddd.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateRolePermissionCmd {
    private UUID roleId;
    private UUID permissionId;
}
