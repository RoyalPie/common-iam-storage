package com.evo.ddd.domain.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UpdateUserCmd {
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private int yearsOfExperience;
    private List<CreateUserRoleCmd> userRole;
}
