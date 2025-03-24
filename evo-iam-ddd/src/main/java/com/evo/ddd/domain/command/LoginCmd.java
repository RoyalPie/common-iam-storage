package com.evo.ddd.domain.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginCmd {
    private String username;
    private String password;
}
