package com.evo.iam.keycloak;

import com.evo.common.dto.TokenDTO;
import com.evo.common.dto.response.Response;
import com.evo.iam.dto.request.GetTokenRequest;
import feign.QueryMap;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "keycloak-identity-client", url = "${keycloak.server-url:}")
public interface KeyCloakClient {

    @Retry(name = "keycloakClientRetry", fallbackMethod = "getTokenFallback")
    @CircuitBreaker(name = "keycloakClientCircuitBreaker", fallbackMethod = "getTokenFallback")
    @PostMapping(value = "/realms/testing-realm/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO getToken(@QueryMap GetTokenRequest param);

    @Retry(name = "keycloakClientRetry", fallbackMethod = "getUserIdFromKeycloakFallback")
    @CircuitBreaker(name = "keycloakClientCircuitBreaker", fallbackMethod = "getUserIdFromKeycloakFallback")
    @GetMapping(value = "admin/realms/testing-realm/users")
    Response<String> getUserIdFromKeycloak(@RequestHeader("authorization") String token, @RequestParam("username") String username);

    default TokenDTO getTokenFallback(GetTokenRequest param, Throwable throwable) {
        return new TokenDTO("default-token", "default-refresh-token");
    }

    default Response<String> getUserIdFromKeycloakFallback(String token, String username, Throwable throwable) {
        return new Response<>();
    }
}