package com.evo.elasticsearch.domain.command;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncUserCmd {
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
}
