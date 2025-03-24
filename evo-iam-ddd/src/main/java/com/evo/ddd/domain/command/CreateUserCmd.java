package com.evo.ddd.domain.command;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCmd {
    private UUID providerId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private int yearsOfExperience;
    private CreateUserRoleCmd userRole;
}
