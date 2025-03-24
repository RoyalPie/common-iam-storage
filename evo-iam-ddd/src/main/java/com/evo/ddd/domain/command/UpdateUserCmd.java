package com.evo.ddd.domain.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserCmd {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private int yearsOfExperience;
}
