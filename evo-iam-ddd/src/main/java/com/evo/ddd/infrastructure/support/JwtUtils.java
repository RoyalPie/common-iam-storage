package com.evo.ddd.infrastructure.support;


import com.evo.ddd.application.config.AuthenticationProperties;
import com.evo.ddd.application.config.TokenProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TokenProvider tokenProvider;
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

    public JWTClaimsSet extractClaims(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) tokenProvider.getKeyPair().getPublic());
        boolean isSignatureValid = signedJWT.verify(verifier);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean isExpired = expirationTime != null && expirationTime.before(new Date());
        if(!isSignatureValid && isExpired){
            return null;
        }
        return signedJWT.getJWTClaimsSet();
    }
    public String extractEmailFromToken(String token) throws ParseException, JOSEException {
        JWTClaimsSet claimsSet = extractClaims(token);
        return claimsSet.getSubject().isEmpty() ? claimsSet.getClaim("username").toString() : claimsSet.getSubject();
    }
    public boolean validateToken(String token, String username) throws ParseException, JOSEException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) tokenProvider.getKeyPair().getPublic());

            boolean isSignatureValid = signedJWT.verify(verifier);

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isExpired = expirationTime != null && expirationTime.before(new Date());

            String verifyEmail = signedJWT.getJWTClaimsSet().getSubject();

            return isSignatureValid && !isExpired && Objects.equals(verifyEmail, username);

        } catch (Exception e) {
            System.out.println("JWT verification failed: " + e.getMessage());
            return false;
        }
    }

}
