package com.evo.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = getBearerToken();
        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }

    private String getBearerToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getCredentials() instanceof Jwt) {
            return ((Jwt) authentication.getCredentials()).getTokenValue();
        }
        return null;
    }
}