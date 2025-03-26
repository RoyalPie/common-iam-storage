package com.evo.ddd.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID userID;
    private UUID providerId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String dob;
    private String street;
    private String ward;
    private String district;
    private String city;
    private int yearsOfExperience;
    private UUID avatarFileId;
}
