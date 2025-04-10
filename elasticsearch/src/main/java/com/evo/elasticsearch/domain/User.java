package com.evo.elasticsearch.domain;

import com.evo.elasticsearch.domain.command.SyncUserCmd;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class User {
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
    private boolean active;
    private boolean deleted;

    public User(SyncUserCmd cmd){
        this.userID = cmd.getUserID();
        this.keycloakUserId = cmd.getKeycloakUserId();
        this.username = cmd.getUsername();
        this.email = cmd.getEmail();
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.dateOfBirth = cmd.getDateOfBirth();
        this.address = cmd.getAddress();
        this.yearsOfExperience = cmd.getYearsOfExperience();
        this.active = cmd.isActive();
        this.deleted = cmd.isDeleted();
    }
    public void update(SyncUserCmd cmd) {
        if(cmd.getEmail() != null) {
            this.email = cmd.getEmail();
        }
        if(cmd.getFirstName() != null) {
            this.firstName = cmd.getFirstName();
        }
        if(cmd.getLastName() != null) {
            this.lastName = cmd.getLastName();
        }
        if(cmd.getAvatarFileId() != null) {
            this.avatarFileId = cmd.getAvatarFileId();
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
        if(cmd.isActive() != false) {
            this.active = cmd.isActive();
        }
        if(cmd.isDeleted() != false) {
            this.deleted = cmd.isDeleted();
        }
    }
}

