package com.evo.ddd.domain;

import com.evo.common.Auditor;

import com.evo.ddd.domain.command.CreateUserCmd;
import com.evo.ddd.domain.command.UpdateUserCmd;
import com.evo.ddd.infrastructure.support.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class User extends Auditor {
    private UUID userID;
    private UUID providerId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UUID avatarFileId;
    private LocalDate dob;
    private String address;
    private int yearsOfExperience;
    private String password;
    private boolean locked;
    private UserRole userRole;
    private UserActivityLog userActivityLog;

    public User(CreateUserCmd cmd) {
        this.userID = IdUtils.newUUID();
        this.username = cmd.getUsername();
        this.password = cmd.getPassword();
        this.email = cmd.getEmail();
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.dob = cmd.getDob();
        this.address = cmd.getAddress();
        this.yearsOfExperience = cmd.getYearsOfExperience();
        this.locked = false;
        this.providerId = cmd.getProviderId();
        if(cmd.getUserRole() != null) {
            this.userRole = new UserRole(cmd.getUserRole(), this.userID);
        }
    }

    public void update(UpdateUserCmd cmd) {
        if(cmd.getEmail() != null) {
            this.email = cmd.getEmail();
        }
        if(cmd.getFirstName() != null) {
            this.firstName = cmd.getFirstName();
        }
        if(cmd.getLastName() != null) {
            this.lastName = cmd.getLastName();
        }
        if(cmd.getDob() != null) {
            this.dob = cmd.getDob();
        }
        if(cmd.getAddress() != null) {
            this.address = cmd.getAddress();
        }
        if(cmd.getYearsOfExperience() != 0) {
            this.yearsOfExperience = cmd.getYearsOfExperience();
        }
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;

    }

    public void changeAvatar(UUID fileId) {
        this.avatarFileId = fileId;
    }
}
