package com.evo.ddd.infrastructure.adapter.keycloak;

import com.evo.ddd.application.dto.request.CreateUserRequest;
import com.evo.ddd.application.dto.request.identityKeycloak.CreateUserKeycloakRequest;
import com.evo.ddd.application.dto.request.identityKeycloak.CredentialRequest;
import com.evo.ddd.domain.command.ResetKeycloakPasswordCmd;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakCommandClientImpl implements KeycloakCommandClient {
    private final KeycloakIdentityClient keycloakIdentityClient;
    private final KeycloakQueryClient keycloakQueryClient;

    @Override
    public String createKeycloakUser(CreateUserRequest request) {
        try {
            String token = keycloakQueryClient.getClientToken();
            var creationResponse = keycloakIdentityClient.createUser(
                    "Bearer " + token,
                    CreateUserKeycloakRequest.builder()
                            .username(request.getUsername())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .email(request.getEmail())
                            .enabled(true)
                            .emailVerified(false)

                            .credentials(List.of(CredentialRequest.builder()
                                    .type("password")
                                    .temporary(false)
                                    .value(request.getPassword())
                                    .build()))
                            .build());

            return extractUserId(creationResponse);
        } catch (FeignException e) {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public void resetPassword(String token, UUID keycloakUserId, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        try {
            keycloakIdentityClient.resetPassword(
                    "Bearer " + token,
                    keycloakUserId.toString(),
                    resetKeycloakPasswordCmd);
        } catch (FeignException e) {
            throw new RuntimeException("Error");
        }
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = Optional.ofNullable(response.getHeaders().get("Location"))
                .map(list -> list.isEmpty() ? null : list.getFirst())
                .orElse("");
        String[] splitStr = location.split("/");
        return splitStr[splitStr.length - 1];
    }
}
