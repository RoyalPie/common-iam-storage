package com.evo.ddd.domain;

import com.evo.common.Auditor;

import com.evo.ddd.domain.command.CreateUserCmd;
import com.evo.ddd.domain.command.CreateUserRoleCmd;
import com.evo.ddd.domain.command.UpdateUserCmd;
import com.evo.ddd.infrastructure.support.IdUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class User extends Auditor {
    private UUID userID;
    private UUID keycloakUserId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UUID avatarFileId;
    private Date dateOfBirth;
    private String address;
    private int yearsOfExperience;
    private String password;
    private boolean active;
    private List<UserRole> userRole;
    private UserActivityLog userActivityLog;

    public User(CreateUserCmd cmd) {
        this.userID = IdUtils.newUUID();
        this.username = cmd.getUsername();
        this.password = cmd.getPassword();
        this.email = cmd.getEmail();
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.dateOfBirth = cmd.getDateOfBirth();
        this.address = cmd.getStreet()+", "+cmd.getWard()+", "+cmd.getDistrict()+", "+cmd.getCity();
        this.yearsOfExperience = cmd.getYearsOfExperience();
        this.active = true;
        this.keycloakUserId = cmd.getKeycloakUserId();
        this.userRole = new ArrayList<>();

        if (cmd.getUserRole() != null) {
            cmd.getUserRole().forEach(createUserRolecmd -> {
                createUserRolecmd.setUserId(this.userID);
                userRole.add(new UserRole(createUserRolecmd));
            });
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
        if(cmd.getDateOfBirth() != null) {
            this.dateOfBirth = cmd.getDateOfBirth();
        }
        if(cmd.getAddress() != null) {
            this.address = cmd.getAddress();
        }
        if(cmd.getYearsOfExperience() != 0) {
            this.yearsOfExperience = cmd.getYearsOfExperience();
        }
        if (cmd.getUserRole() != null && !cmd.getUserRole().isEmpty()) {
            if (this.userRole == null) {
                this.userRole = new ArrayList<>();
            }

            // Map existing roles by roleId
            Map<UUID, UserRole> existingRolesMap = new HashMap<>();
            for (UserRole ur : this.userRole) {
                existingRolesMap.put(ur.getRoleId(), ur);
            }

            // Update or add new roles
            for (CreateUserRoleCmd userRoleCmd : cmd.getUserRole()) {
                UUID roleId = userRoleCmd.getRoleId();
                userRoleCmd.setUserId(this.userID);
                if (!existingRolesMap.containsKey(roleId)) {
                    this.userRole.add(new UserRole(userRoleCmd));
                }
            }
        }

        UserActivityLog log = new UserActivityLog();
        log.setActivity("Update User");
        this.setUserActivityLog(log);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;

    }

    public void changeAvatar(UUID fileId) {
        this.avatarFileId = fileId;
    }

}
