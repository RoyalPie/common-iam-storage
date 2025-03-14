package com.evo.iam.controller;


import com.evo.iam.config.TokenProvider;
import com.evo.iam.entity.EmailDetails;
import com.evo.iam.entity.RefreshToken;
import com.evo.iam.entity.Role;
import com.evo.iam.entity.User;
import com.evo.iam.payload.request.LoginRequest;
import com.evo.iam.payload.request.OtpRequest;
import com.evo.iam.payload.request.RefreshTokenRequest;
import com.evo.iam.payload.request.SignupRequest;
import com.evo.iam.payload.response.JwtResponse;
import com.evo.iam.payload.response.MessageResponse;
import com.evo.iam.repository.RoleRepository;
import com.evo.iam.repository.UserRepository;
import com.evo.iam.service.*;
import com.evo.iam.service.IService.LoginService;
import com.evo.iam.service.IService.LogoutService;
import com.evo.iam.service.refreshTokenService.KeycloakRefreshTokenService;
import com.evo.iam.service.refreshTokenService.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final LogoutService logoutService;

    private final LoginService loginService;

    private final KeycloakService keycloakService;

    private final RefreshTokenService refreshTokenService;

    private final EmailService emailService;

    private final OtpService otpService;

    private final UserActivityLogService userActivityLogService;

    private final KeycloakRefreshTokenService keycloakRefreshTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    TokenProvider tokenProvider;

    @Value("${default.profilePicture}")
    private String defaultProfilePicture;

    @Value("${keycloak.enabled}")
    private Boolean keycloakEnabled;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        return loginService.authenticate(loginRequest);
    }

    @PostMapping("/sign-in/verify-otp")
    public ResponseEntity<?> authenticateOtp(@RequestBody OtpRequest otpRequest, HttpServletRequest request) {
        String otp = otpRequest.getOtp();
        String email = otpRequest.getEmail();
        try {
            if (otpService.verifyOtp(email, otp)) {
                String jwt = tokenProvider.createAccessToken(email);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);
                String ip = request.getRemoteAddr();
                String userAgent = request.getHeader("User-Agent");
                userActivityLogService.logActivity(userService.findbyEmail(email).orElseThrow(() -> new UsernameNotFoundException("Not found user with this email")), "LOGIN", ip, userAgent);

                return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), email));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP code");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while verifying OTP: " + e.getMessage());
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Default role 'USER' is not found."));
        roles.add(defaultRole);

        user.setRoles(roles);
        user.setAddress(signUpRequest.getAddress());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());

        user.setProfilePicturePath(defaultProfilePicture);
        userRepository.save(user);

        try {
            String keycloakUserId = keycloakService.createUserInKeycloak(signUpRequest);
            user.setKeycloakUserId(keycloakUserId);
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Failed to create user in Keycloak!"));
        }

        EmailDetails email = new EmailDetails(user.getEmail(), "Welcome new User" + user.getUsername(), "Successful Registration");
        emailService.sendSimpleMail(email);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        String newToken = null;
        if (keycloakEnabled) {
            newToken = keycloakRefreshTokenService.refreshToken(requestRefreshToken);
        } else {
            newToken = refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        try {
                            return tokenProvider.createAccessToken(user.getEmail());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElseThrow(() -> new IllegalArgumentException("Refresh token is not in database!"));
        }
        return ResponseEntity.ok(new MessageResponse("New JWT Token: " + newToken));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, Authentication authentication) throws Exception {
        return logoutService.logout(request, authentication);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotpassword(@RequestBody EmailDetails details) {
        String resetToken = null;
        try {
            resetToken = tokenProvider.createAccessToken(details.getRecipient());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        details.setMsgBody("\nPlease click the link below to reset your password\n\nhttp://localhost:8080/api/auth/verify-reset-token?" + "email=" + details.getRecipient() + "&token=" + resetToken);
        details.setSubject("Change Password Mail");
        return ResponseEntity.ok(emailService.sendSimpleMail(details));
    }

//    @GetMapping("/verify-reset-token")
//    public ResponseEntity<String> verifyResetToken(@RequestParam String token, @RequestBody ChangePasswordRequest request) {
//
//        if (token.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Reset token not found");
//        }
//
//        try {
//            String email = jwtUtils.extractEmail(token);
//            if (jwtUtils.validateToken(token, email) && !jwtUtils.isTokenExpired(token)) {
//                userService.updatePassword(userRepository.findByEmail(email).map(User::getId).orElseThrow(() -> new UsernameNotFoundException("Not found")), request);
//                return ResponseEntity.ok("Directed to change password page");
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid reset token");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}
