package com.evo.iam.service.loginService;

import com.evo.iam.entity.CustomUserDetails;
import com.evo.iam.payload.request.LoginRequest;
import com.evo.iam.payload.response.MessageResponse;
import com.evo.iam.repository.UserRepository;
import com.evo.iam.service.IService.LoginService;
import com.evo.iam.service.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("customLoginService")
public class CustomLoginService implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final UserRepository userRepository;

    public CustomLoginService(AuthenticationManager authenticationManager, OtpService otpService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.otpService = otpService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        if (userRepository.existsByEmail(loginRequest.getUsername()) && userRepository.isActive(loginRequest.getUsername())) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                return ResponseEntity.ok(new MessageResponse(otpService.generateAndSendOtp(userDetails.getEmail())));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not active account");
    }
}
