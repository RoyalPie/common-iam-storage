package com.evo.iam.keycloak;

import com.evo.common.dto.TokenDTO;
import com.evo.iam.dto.request.GetTokenRequest;
import feign.QueryMap;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "keycloak-identity-client", url = "${keycloak.server-url:}")
public interface KeyCloakClient {
    @LoadBalanced
    @PostMapping(value = "/realms/testing-realm/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenDTO getToken(@QueryMap GetTokenRequest param);
}
