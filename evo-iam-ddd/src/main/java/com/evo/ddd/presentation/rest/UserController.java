package com.evo.ddd.presentation.rest;

import com.evo.common.dto.response.ApiResponses;
import com.evo.ddd.application.dto.request.ChangePasswordRequest;
import com.evo.ddd.application.dto.request.UpdateUserRequest;
import com.evo.ddd.application.dto.response.UserDTO;
import com.evo.ddd.application.service.UserCommandService;
import com.evo.ddd.application.service.UserQueryService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping("/my-info")
    public ApiResponses<UserDTO> getMyInfo(Authentication authentication) {
        String username = authentication.getName();
        UserDTO userDTO = userQueryService.getUserInfo(username);
        return ApiResponses.<UserDTO>builder()
                .data(userDTO)
                .success(true)
                .code(200)
                .message("Get my info successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PreAuthorize("hasPermission(null, 'user.update')")
    @PutMapping("/update")
    public ApiResponses<UserDTO> updateUser(@Parameter(description = "username of user account", required = true)
                                            @RequestBody UpdateUserRequest updateUserRequest,
                                            Authentication authentication) {
        String username = authentication.getName();
        UserDTO userDTO = userCommandService.updateUser(username, updateUserRequest);
        return ApiResponses.<UserDTO>builder()
                .data(userDTO)
                .success(true)
                .code(200)
                .message("Update user successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PatchMapping("/change-password")
    public ApiResponses<Void> changePassword(Authentication authentication,
                                             @RequestBody ChangePasswordRequest changePasswordRequest) {
        String username = authentication.getName();
        userCommandService.changePassword(username, changePasswordRequest);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Password successfully changed")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PostMapping("/change-avatar")
    public ApiResponses<Void> changeAvatar(Authentication authentication,
                                           @RequestParam List<MultipartFile> files){
        String username = authentication.getName();
        userCommandService.changeAvatar(username, files);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Avatar successfully changed")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
