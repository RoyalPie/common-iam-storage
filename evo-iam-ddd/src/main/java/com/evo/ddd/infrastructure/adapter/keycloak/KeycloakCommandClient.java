package com.evo.ddd.infrastructure.adapter.keycloak;

import com.evo.ddd.application.dto.request.CreateUserRequest;
import com.evo.ddd.domain.command.ResetKeycloakPasswordCmd;

import java.util.UUID;

public interface KeycloakCommandClient {
    String createKeycloakUser(CreateUserRequest request);
    void resetPassword(String token, UUID keycloakUserId, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);
}
