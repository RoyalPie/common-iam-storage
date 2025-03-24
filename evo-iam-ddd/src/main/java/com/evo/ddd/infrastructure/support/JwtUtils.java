package com.evo.ddd.infrastructure.support;


import com.evo.ddd.application.config.AuthenticationProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtils {
    private final AuthenticationProperties properties;
    private KeyPair keyPair;

    public JwtUtils(AuthenticationProperties properties) {
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

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());

            Certificate cert = keyStore.getCertificate(alias);
            PublicKey publicKey = cert.getPublicKey();

            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load key pair from keystore", e);
        }
    }
    public static String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }
    public static String getTokenSignature(String token) throws NoSuchAlgorithmException {
        String[] parts = token.split("\\."); // JWT format: header.payload.signature
        String signature = parts[2];

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(signature.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
    public boolean validateResetToken(String token, String email) throws ParseException, JOSEException {

        PublicKey publicKey = keyPair.getPublic();
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
            boolean isSignatureValid = signedJWT.verify(verifier);

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isExpired = expirationTime != null && expirationTime.before(new Date());

            String verifyEmail = signedJWT.getJWTClaimsSet().getSubject();

            return isSignatureValid && !isExpired && Objects.equals(verifyEmail, email);

        } catch (Exception e) {
            System.out.println("JWT verification failed: " + e.getMessage());
            return false;
        }
    }
}
