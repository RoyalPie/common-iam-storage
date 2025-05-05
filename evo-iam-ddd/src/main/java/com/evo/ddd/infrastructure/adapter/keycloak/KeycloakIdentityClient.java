package com.evo.ddd.infrastructure.adapter.keycloak;

import com.evo.ddd.application.dto.request.identityKeycloak.GetTokenRequest;
import com.evo.ddd.application.dto.request.identityKeycloak.LogoutRequest;
import com.evo.ddd.application.dto.request.UpdateUserRequest;
import com.evo.ddd.application.dto.request.identityKeycloak.CreateUserKeycloakRequest;
import com.evo.ddd.application.dto.request.identityKeycloak.RefreshTokenRequest;
import com.evo.ddd.application.dto.response.TokenDTO;
import com.evo.ddd.domain.command.LockUserCmd;
import com.evo.ddd.domain.command.ResetKeycloakPasswordCmd;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "identity-client", url = "${keycloak.server-url}")
public interface KeycloakIdentityClient {
    @PostMapping(value = "/realms/testing-realm/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO getToken(@QueryMap GetTokenRequest param);

    @PostMapping(value = "/admin/realms/testing-realm/users",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token,
            @RequestBody CreateUserKeycloakRequest param);

    @PostMapping(value = "/realms/testing-realm/protocol/openid-connect/logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void logout(
            @RequestHeader("authorization") String token,
            @QueryMap LogoutRequest logoutRequest);

    @PostMapping(value = "/realms/testing-realm/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO refreshToken(@QueryMap RefreshTokenRequest refreshTokenRequest);

    @PutMapping(value = "/admin/realms/testing-realm/users/{user_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateUser(@RequestHeader("authorization") String token,
                  @PathVariable("user_id") String userId,
                  @RequestBody UpdateUserRequest updateUserRequest);

    @PutMapping(value = "/admin/realms/testing-realm/users/{user_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void lockUser(@RequestHeader("authorization") String token,
                    @PathVariable("user_id") String userId,
                    @RequestBody LockUserCmd lockUserCmd);

    @PutMapping(value = "/admin/realms/testing-realm/users/{user_id}/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void resetPassword(@RequestHeader("authorization") String token,
                    @PathVariable("user_id") String userId,
                    @RequestBody ResetKeycloakPasswordCmd resetKeycloakPasswordCmd);

    @GetMapping(value = "/realms/testing-realm/protocol/openid-connect/userinfo")
    ResponseEntity<Map<String, String>> getUserInfo(@RequestHeader("authorization") String token);
}
