package com.evo.ddd.application.service.impl.command;

import com.evo.ddd.application.config.TokenProvider;
import com.evo.ddd.application.dto.request.LoginRequest;
import com.evo.ddd.application.dto.request.VerifyOtpRequest;
import com.evo.ddd.application.dto.request.identityKeycloak.GetTokenRequest;
import com.evo.ddd.application.dto.response.TokenDTO;
import com.evo.ddd.application.mapper.CommandMapper;
import com.evo.ddd.application.service.AuthServiceCommand;
import com.evo.ddd.domain.User;
import com.evo.ddd.domain.UserActivityLog;
import com.evo.ddd.domain.command.LoginCmd;
import com.evo.ddd.domain.command.ResetKeycloakPasswordCmd;
import com.evo.ddd.domain.command.VerifyOtpCmd;
import com.evo.ddd.domain.command.WriteLogCmd;
import com.evo.ddd.domain.repository.UserDomainRepository;
import com.evo.ddd.infrastructure.adapter.keycloak.KeycloakIdentityClient;
import com.evo.ddd.infrastructure.adapter.mail.EmailService;
import com.evo.ddd.infrastructure.support.JwtUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.evo.common.webapp.security.TokenCacheService.INVALID_TOKEN_CACHE;

@Component("self_idp_auth_service")
@RequiredArgsConstructor
public class SelfIDPAuthServiceCommandImpl implements AuthServiceCommand {
    private final CommandMapper commandMapper;
    private final UserDomainRepository userDomainRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final KeycloakIdentityClient keycloakIdentityClient;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;
    private final JwtUtils jwtUtils;


    @Value("${otp.expiration}")
    private Long otpExpiration;
    @Value("${jwt.RefreshExpirationMs}")
    private Long refreshExpiration;
    @Value("${keycloak.iam.client-id}")
    private String clientId;
    @Value("${keycloak.iam.client-secret}")
    private String clientSecret;

    @Override
    public TokenDTO authenticate(LoginRequest loginRequest) {
        LoginCmd loginCmd = commandMapper.from(loginRequest);
        User user = userDomainRepository.getByUsername(loginCmd.getUsername());
        if (user == null || !passwordEncoder.matches(loginCmd.getPassword(), user.getPassword())) {
            throw new RuntimeException("INVALID_CREDENTIALS");
        }
        if (redisTemplate.hasKey(user.getUsername())) {
            throw new RuntimeException("ALREADY SEND OTP CHECK MAIL");
        }
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        redisTemplate.opsForValue().set(user.getUsername(), String.valueOf(otp), otpExpiration, TimeUnit.MINUTES);
        emailService.sendMailOtp(user.getEmail(), String.valueOf(otp));

        return null;
    }
    public TokenDTO verifyOTP(VerifyOtpRequest request) throws ParseException, JOSEException {
        VerifyOtpCmd verifyOtpCmd = commandMapper.from(request);

        boolean isValid = Objects.equals(redisTemplate.opsForValue().get(verifyOtpCmd.getUsername()), verifyOtpCmd.getOtp());
        if(!redisTemplate.hasKey(verifyOtpCmd.getUsername()) || !isValid){
            throw new RuntimeException("INVALID_CREDENTIALS");
        }

        User user = userDomainRepository
                .getByUsername((verifyOtpCmd.getUsername()));
        redisTemplate.delete(verifyOtpCmd.getUsername());
        String username = user.getUsername();
        var accessToken = tokenProvider.createAccessToken(username);
        var refreshToken = tokenProvider.createRefreshToken(username);
        redisTemplate.opsForValue().set(jwtUtils.extractClaims(refreshToken).getJWTID(), verifyOtpCmd.getUsername(), refreshExpiration, TimeUnit.MILLISECONDS);
        WriteLogCmd logCmd = commandMapper.from("Login");
        UserActivityLog userActivityLog = new UserActivityLog(logCmd);
        user.setUserActivityLog(userActivityLog);
        userDomainRepository.save(user);
        return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();

    }
    @Override
    public void logoutIam(HttpServletRequest request, String refreshToken) {
        try{
            String accessToken = JwtUtils.extractTokenFromRequest(request);
            JWTClaimsSet accessClaims = jwtUtils.extractClaims(accessToken);
            String username = accessClaims.getClaim("username").toString();
            if(jwtUtils.validateToken(accessToken, username)){
                String refreshId = jwtUtils.extractClaims(refreshToken).getJWTID();
                redisTemplate.delete(refreshId);
                long timeRemain = accessClaims.getExpirationTime().getTime() - System.currentTimeMillis();
                assert accessToken != null;
                redisTemplate.opsForValue().set(INVALID_TOKEN_CACHE, accessToken , timeRemain, TimeUnit.MILLISECONDS);
            }
        }catch (Exception e){
            throw new RuntimeException("Wrong token");
        }
    }

    @Override
    public TokenDTO refresh(String refreshToken) {
        try {
            JWTClaimsSet claims = jwtUtils.extractClaims(refreshToken);
            String username = claims.getSubject();
            var accessToken = tokenProvider.createAccessToken(username);
            return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestPasswordReset(String username, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        try {
            User user = userDomainRepository.getByUsername(username);
            String token = tokenProvider.createResetToken(username);
            String resetLink = "http://localhost:8888/api/authenticate/reset-password?token=" + token;
            emailService.sendMailForResetPassWord(user.getEmail(), resetLink);
        } catch (Exception ex) {
            throw new RuntimeException("Cant make request");
        }
    }

    @Override
    public void resetPassword(String token, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {
        try {
            JWTClaimsSet claims = jwtUtils.extractClaims(token);
            String username = claims.getSubject();
            User user = userDomainRepository.getByUsername(username);
            user.changePassword(passwordEncoder.encode(resetKeycloakPasswordCmd.getValue()));

            var keycloakToken = keycloakIdentityClient.getToken(GetTokenRequest.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());
            keycloakIdentityClient.resetPassword("Bearer " + keycloakToken.getAccessToken(), user.getKeycloakUserId().toString(), resetKeycloakPasswordCmd);
            WriteLogCmd cmd = commandMapper.from("Change password");
            UserActivityLog userActivityLog = new UserActivityLog(cmd);
            user.setUserActivityLog(userActivityLog);
            userDomainRepository.save(user);
            emailService.sendMailAlert(user.getEmail(), "change_password");

        } catch (Exception ex) {
            throw new RuntimeException("Cant make request");
        }
    }
}
