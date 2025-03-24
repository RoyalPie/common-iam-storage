package com.evo.ddd.domain;

import com.evo.common.Auditor;
import com.evo.ddd.domain.command.CreateOrUpdatePermissionCmd;
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
public class Permission extends Auditor {
    private UUID id;
    private String resource;
    private String scope;
    private  boolean deleted;

    public Permission(CreateOrUpdatePermissionCmd cmd) {
        this.id = IdUtils.newUUID();
        this.resource = cmd.getResource();
        this.scope = cmd.getScope();
        this.deleted = false;
    }

    public Permission update(CreateOrUpdatePermissionCmd cmd) {
        this.resource = cmd.getResource();
        this.scope = cmd.getScope();
        return this;
    }
}
