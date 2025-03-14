package com.evo.iam.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface LogoutService {
    ResponseEntity<?> logout(HttpServletRequest request, Authentication authentication)throws Exception;
}
