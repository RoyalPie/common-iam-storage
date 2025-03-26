package com.evo.ddd.application.service;

import com.evo.ddd.application.dto.request.LoginRequest;
import com.evo.ddd.application.dto.response.TokenDTO;
import com.evo.ddd.domain.command.ResetKeycloakPasswordCmd;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthServiceCommand {
    TokenDTO authenticate(LoginRequest loginRequest);
    void logoutIam(HttpServletRequest request, String refreshToken);
    TokenDTO refresh(String refreshToken);
    void requestPasswordReset(String username, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);
    void resetPassword(String token, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);
}
