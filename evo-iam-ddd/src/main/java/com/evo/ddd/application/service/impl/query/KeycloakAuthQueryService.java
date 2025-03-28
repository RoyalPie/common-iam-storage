package com.evo.ddd.application.service.impl.query;

import com.evo.ddd.application.config.TokenProvider;
import com.evo.ddd.application.service.AuthServiceQuery;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component("keycloak_auth_query_service")
@RequiredArgsConstructor
public class KeycloakAuthQueryService implements AuthServiceQuery {
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getClientToken(String clientId, String clientSecret) {
        return null;
    }
}