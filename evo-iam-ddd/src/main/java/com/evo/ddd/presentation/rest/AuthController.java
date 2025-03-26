package com.evo.ddd.presentation.rest;

import com.evo.common.dto.response.ApiResponses;
import com.evo.ddd.application.dto.request.CreateUserRequest;
import com.evo.ddd.application.dto.request.LoginRequest;
import com.evo.ddd.application.dto.request.VerifyOtpRequest;
import com.evo.ddd.application.dto.response.TokenDTO;
import com.evo.ddd.application.dto.response.UserDTO;
import com.evo.ddd.application.service.UserCommandService;
import com.evo.ddd.application.service.impl.command.SelfIDPAuthServiceCommandImpl;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor
public class AuthController {
    private final SelfIDPAuthServiceCommandImpl authServiceCommand;
    private final UserCommandService userCommandService;
    @PostMapping("/login")
    public ApiResponses<TokenDTO> loginIam(@RequestBody LoginRequest loginRequest) {
        TokenDTO tokenDTO = authServiceCommand.authenticate(loginRequest);
        return ApiResponses.<TokenDTO>builder()
                .data(tokenDTO)
                .success(true)
                .code(201)
                .message("OTP sent to your Email")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PostMapping("/verify-otp")
    public ApiResponses<TokenDTO> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) throws ParseException {
        TokenDTO result = authServiceCommand.verifyOTP(verifyOtpRequest);
        return ApiResponses.<TokenDTO>builder()
                .data(result)
                .success(true)
                .code(200)
                .message("OTP đã được xác nhận thành công")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PostMapping("/logout")
    ApiResponses<String> logoutIam(@Parameter(description = "request", hidden = true) HttpServletRequest request,
                                   @Parameter(description = "Refresh token từ client", required = true) @RequestParam String refreshToken
    ) {
        authServiceCommand.logoutIam(request, refreshToken);
        return ApiResponses.<String>builder()
                .data("Logout successful")
                .success(true)
                .code(200)
                .message("Logout successful")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PostMapping("/users")
    public ApiResponses<UserDTO> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserDTO userDTO = userCommandService.createDefaultUser(createUserRequest);
        return ApiResponses.<UserDTO>builder()
                .data(userDTO)
                .success(true)
                .code(201)
                .message("User created successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
