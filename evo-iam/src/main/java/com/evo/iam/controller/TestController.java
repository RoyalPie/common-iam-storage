package com.evo.iam.controller;


import com.evo.iam.service.refreshTokenService.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @GetMapping("/asas")
    public Map<String, Object> getTokens(Authentication authentication, @RequestParam(required = false) String email) throws Exception {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication + "   0");
        System.out.println(authentication1 + "    1");
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                "keycloak", "truongtk1711@gmail.com"
        );
        return Map.of(
                "access-token", client.getAccessToken().getTokenValue(),
                "refresh-token", client.getRefreshToken().getTokenValue()
        );

    }

    @GetMapping("/")
    public List<String> getAllOAuth2Clients() {
        List<String> clientNames = new ArrayList<>();

        if (clientRegistrationRepository instanceof Iterable) {
            Iterable<ClientRegistration> registrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
            for (ClientRegistration registration : registrations) {
                clientNames.add(registration.getRegistrationId());
            }
        }
        System.out.println(clientNames);
        return clientNames;
    }
}
