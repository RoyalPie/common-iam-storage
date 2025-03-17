package com.evo.iam.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@EnableConfigurationProperties(AuthenticationProperties.class)
@Slf4j
public class TokenProvider {
    private final AuthenticationProperties properties;
    private KeyPair keyPair;

    public TokenProvider(AuthenticationProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    private void initKeyPair() {
        this.keyPair = loadKeyPair(this.properties.getKeyStore(), this.properties.getKeyStorePassword(), this.properties.getKeyAlias());
    }

    private KeyPair loadKeyPair(String keyStorePath, String password, String alias) {
        try (InputStream inputStream = new ClassPathResource(keyStorePath).getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, password.toCharArray());

            // Get Private Key
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());

            // Get Public Key from Certificate
            Certificate cert = keyStore.getCertificate(alias);
            PublicKey publicKey = cert.getPublicKey();

            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load key pair from keystore", e);
        }
    }

    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) this.keyPair.getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(UUID.randomUUID().toString());
        return new JWKSet(builder.build());
    }

    private Date getExpirationDate(long durationMillis) {
        return new Date(Instant.now().toEpochMilli() + durationMillis);
    }

    private String createToken(String email, long durationMillis) {
        return Jwts.builder()
                .setSubject(email)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(durationMillis))
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
    }

    public String createAccessToken(String email) {
        return createToken(email, properties.getAccessTokenExpiresIn().toMillis());
    }

    public String createRefreshToken(String email) {
        return createToken(email, properties.getRefreshTokenExpiresIn().toMillis());
    }

    public String createResetToken(String email) {
        return createToken(email, properties.getResetTokenExpiresIn().toMillis());
    }
}
