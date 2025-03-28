package com.evo.ddd.domain.command;

import com.evo.ddd.domain.UserRole;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCmd {
    private UUID keycloakUserId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String street;
    private String ward;
    private String district;
    private String city;
    private int yearsOfExperience;
    private List<CreateUserRoleCmd> userRole;
}
