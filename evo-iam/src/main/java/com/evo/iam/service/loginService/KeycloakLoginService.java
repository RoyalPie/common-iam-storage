package com.evo.iam.service.loginService;


import com.evo.common.dto.TokenDTO;
import com.evo.iam.dto.request.GetTokenRequest;
import com.evo.iam.keycloak.KeyCloakClient;
import com.evo.iam.payload.request.LoginRequest;
import com.evo.iam.service.IService.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("keycloakLoginService")
@RequiredArgsConstructor
public class KeycloakLoginService implements LoginService {
    private final KeyCloakClient keyCloakClient;

    @Value("${keycloak.iam.client-id}")
    private String clientId;
    @Value("${keycloak.iam.client-secret}")
    private String clientSecret;

    @Override
    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        TokenDTO tokenDTO = keyCloakClient.getToken(
                GetTokenRequest.builder()
                        .grant_type("password")
                        .client_id(clientId)
                        .client_secret(clientSecret)
                        .scope("openid")
                        .username(loginRequest.getUsername())
                        .password(loginRequest.getPassword())
                        .build());

        return ResponseEntity.ok(tokenDTO);
    }
}
