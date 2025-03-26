package com.evo.ddd.presentation.resource;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.Response;
import com.evo.ddd.application.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserResource {
    private final UserQueryService userQueryService;
//    @GetMapping("/{userId}/authority")
//    public Response<UserAuthority> getUserAuthority(@PathVariable UUID userId) {
//
//    }

    @GetMapping("/{username}/authorities-by-username")
    public Response<UserAuthority> getUserAuthorityByUsername(@PathVariable String username) {
        UserAuthority userAuthority = userQueryService.getUserAuthority(username);
        Response<UserAuthority> response = new Response<UserAuthority>().success();
        response.setData(userAuthority);
        return response;
    }

    @PostMapping("/revoke-refresh-token")
    public void revokeRefreshToken(@RequestBody String refreshToken){

    }
}
