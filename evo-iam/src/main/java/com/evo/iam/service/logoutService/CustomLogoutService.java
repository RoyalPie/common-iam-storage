package com.evo.iam.service.logoutService;

import com.evo.common.webapp.security.impl.TokenCacheServiceImpl;
import com.evo.iam.entity.User;
import com.evo.iam.payload.response.MessageResponse;
import com.evo.iam.repository.UserRepository;
import com.evo.iam.service.IService.LogoutService;
import com.evo.iam.service.UserActivityLogService;
import com.evo.iam.service.refreshTokenService.RefreshTokenService;
import com.evo.iam.support.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customLogoutService")
@RequiredArgsConstructor
public class CustomLogoutService implements LogoutService {
    private final UserRepository userRepository;
    private final UserActivityLogService userActivityLogService;
    private final RefreshTokenService refreshTokenService;
    private final TokenCacheServiceImpl tokenCacheService;

    @Override
    public ResponseEntity<?> logout(HttpServletRequest request, Authentication authentication) throws Exception {
        String token = JwtUtils.extractTokenFromRequest(request);
        tokenCacheService.invalidToken(token);
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("User not found"));
        refreshTokenService.deleteByUser(user.getEmail());

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        userActivityLogService.logActivity(user, "LOGOUT", ip, userAgent);
        return ResponseEntity.ok(new MessageResponse("Logout successfully"));
    }

}
