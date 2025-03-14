package com.evo.iam.service.loginService;


import com.evo.iam.payload.request.LoginRequest;
import com.evo.iam.service.IService.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("keycloakLoginService")
public class KeycloakLoginService implements LoginService {

    @Override
    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        return ResponseEntity.ok("Please log in via Keycloak: http://localhost:8082/login/oauth2/code/keycloak ");
    }
}
