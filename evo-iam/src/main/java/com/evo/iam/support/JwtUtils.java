package com.evo.iam.support;

import com.evo.iam.config.AuthenticationProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
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
        keyPair = keyPair(this.properties.getKeyStore(), this.properties.getKeyStorePassword(), this.properties.getKeyAlias());
    }

    private KeyPair keyPair(String keyStore, String password, String alias) {
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(
                        new ClassPathResource(keyStore),
                        password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(alias);
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
