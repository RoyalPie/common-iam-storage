package com.evo.ddd.application.dto.request;

import com.evo.ddd.domain.command.CreateUserRoleCmd;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private int yearsOfExperience;
    private List<CreateUserRoleRequest> userRole;
}
