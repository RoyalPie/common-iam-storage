package com.evo.ddd.domain;

import com.evo.common.Auditor;
import com.evo.ddd.domain.command.WriteLogCmd;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class UserActivityLog extends Auditor {
    private UUID id;
    private String activity;

    public UserActivityLog(WriteLogCmd cmd) {
        this.activity = cmd.getActivity();
    }
}
