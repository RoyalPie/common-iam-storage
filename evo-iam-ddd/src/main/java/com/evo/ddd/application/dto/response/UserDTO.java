package com.evo.ddd.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID userID;
    private UUID keycloakUserId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private int yearsOfExperience;
    private UUID avatarFileId;
}
