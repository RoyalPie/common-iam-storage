package com.evo.ddd.application.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
