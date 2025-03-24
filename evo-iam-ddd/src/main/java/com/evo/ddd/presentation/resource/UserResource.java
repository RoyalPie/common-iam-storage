package com.evo.ddd.presentation.resource;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserResource {


//    @GetMapping("/{userId}/authority")
//    public Response<UserAuthority> getUserAuthority(@PathVariable UUID userId) {
//
//    }
//
//    @GetMapping("/{username}/authorities-by-username")
//    public Response<UserAuthority> getUserAuthorityByUsername(@PathVariable String username) {
//
//    }

    @PostMapping("/revoke-refresh-token")
    public void revokeRefreshToken(@RequestBody String refreshToken){

    }
}
