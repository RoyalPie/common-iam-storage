package com.evo.iam.config;

import com.evo.iam.service.IService.LogoutService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogoutServiceConfig {
    @Bean
    public LogoutService logoutService(
            @Value("${keycloak.enabled}") boolean keycloakEnabled,
            @Qualifier("customLogoutService") LogoutService customLogoutService,
            @Qualifier("keycloakLogoutService") LogoutService keycloakLogoutService) {
        return keycloakEnabled ? keycloakLogoutService : customLogoutService;
    }
}