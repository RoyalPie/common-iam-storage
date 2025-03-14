package com.evo.iam.service.IService;

import com.evo.iam.payload.request.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<?> authenticate(LoginRequest loginRequest);
}
