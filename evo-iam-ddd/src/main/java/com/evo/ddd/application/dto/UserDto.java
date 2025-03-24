package com.evo.ddd.application.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String username;
    @Email
    private String email;
    private String profilePicturePath;
    private String phoneNumber;
    private String address;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Boolean isActive;
    private int YoE;
    private Set<RoleDto> roles = new HashSet<>();
}
