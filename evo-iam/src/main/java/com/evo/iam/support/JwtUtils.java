package com.evo.iam.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JwtUtils {
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
}
