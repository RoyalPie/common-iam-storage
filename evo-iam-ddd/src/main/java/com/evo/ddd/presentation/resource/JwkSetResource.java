package com.evo.ddd.presentation.resource;

import com.evo.ddd.application.config.TokenProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwkSetResource {
    private final TokenProvider tokenProvider;

    public JwkSetResource(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/api/certificate/.well-known/jwks.json")
    Map<String, Object> keys() {
        return this.tokenProvider.jwkSet().toJSONObject();
    }
}
