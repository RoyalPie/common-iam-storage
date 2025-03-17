package com.evo.iam.service.refreshTokenService;


import com.evo.iam.config.TokenProvider;
import com.evo.iam.entity.RefreshToken;
import com.evo.iam.entity.User;
import com.evo.iam.repository.RefreshTokenRepository;
import com.evo.iam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.evo.common.webapp.support.SecurityUtils.getJwtExpiry;

@Service
public class RefreshTokenService {
    @Value("${jwt.RefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        RefreshToken refreshToken = new RefreshToken();
        if(refreshTokenRepository.existsByUser(user)){
            refreshToken = refreshTokenRepository.findByUser(user);
        }

        refreshToken.setUser(user);
        refreshToken.setToken(tokenProvider.createRefreshToken(email));
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        try {
            if (System.currentTimeMillis()-getJwtExpiry(token.getToken())<0) {
                refreshTokenRepository.delete(token);
                throw new IllegalStateException("Refresh token was expired. Please make a new sign-in request");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return token;
    }

    @Transactional
    public void deleteByUser(String email) {
        refreshTokenRepository.deleteByUser(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found")));
    }
    @Transactional
    public void deleteByToken(String token) {

        refreshTokenRepository.deleteByToken(token);
    }
}