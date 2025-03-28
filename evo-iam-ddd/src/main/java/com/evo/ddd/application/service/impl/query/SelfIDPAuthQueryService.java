package com.evo.ddd.application.service.impl.query;

import com.evo.ddd.application.config.TokenProvider;
import com.evo.ddd.application.service.AuthServiceQuery;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.util.concurrent.TimeUnit.MINUTES;

@Component("self_idp_auth_query_service")
@RequiredArgsConstructor
@Slf4j
public class SelfIDPAuthQueryService implements AuthServiceQuery {
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public String getClientToken(String clientId, String clientSecret) {
        return null;
    }
}
