package com.evo.ddd.presentation.rest;

import com.evo.common.dto.TokenDTO;
import com.evo.ddd.application.dto.request.GetTokenRequest;
import com.evo.ddd.infrastructure.adapter.keycloak.KeyCloakClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final KeyCloakClient keyCloakClient;

    @Value("${keycloak.iam.client-id}")
    private String clientId;
    @Value("${keycloak.iam.client-secret}")
    private String clientSecret;

    @GetMapping("/api/test/keycloakId")
    ResponseEntity<?> getUserIdFromKeycloak(@RequestParam("username") String username) {
        TokenDTO tokenDTO = keyCloakClient.getToken(GetTokenRequest.builder()
                .grant_type("client_credentials")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .build());

        return ResponseEntity.ok("Bearer " + keyCloakClient.getUserIdFromKeycloak(tokenDTO.getAccessToken(), username));
    }

}
