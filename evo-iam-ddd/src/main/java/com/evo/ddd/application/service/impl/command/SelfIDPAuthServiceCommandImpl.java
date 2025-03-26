package com.evo.ddd.application.service.impl.command;

import com.evo.ddd.application.config.TokenProvider;
import com.evo.ddd.application.dto.request.LoginRequest;
import com.evo.ddd.application.dto.request.VerifyOtpRequest;
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
import com.evo.ddd.infrastructure.adapter.mail.EmailService;
import com.evo.ddd.infrastructure.support.JwtUtils;
import com.nimbusds.jwt.JWTClaimsSet;
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
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;
    private final JwtUtils jwtUtils;


    @Value("${otp.expiration}")
    private Long otpExpiration;

    @Value("${jwt.RefreshExpirationMs}")
    private Long refreshExpiration;

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
    public TokenDTO verifyOTP(VerifyOtpRequest request) throws ParseException {
        VerifyOtpCmd verifyOtpCmd = commandMapper.from(request);

        boolean isValid = Objects.equals(redisTemplate.opsForValue().get(verifyOtpCmd.getUsername()), verifyOtpCmd.getOtp());
        System.out.println(redisTemplate.opsForValue().get(verifyOtpCmd.getUsername()));
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
        return null;
    }

    @Override
    public void requestPasswordReset(String username, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {

    }

    @Override
    public void resetPassword(String token, ResetKeycloakPasswordCmd resetKeycloakPasswordCmd) {

    }
}
