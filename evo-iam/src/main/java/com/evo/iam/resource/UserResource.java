package com.evo.iam.resource;

import com.evo.common.UserAuthority;
import com.evo.common.dto.response.Response;
import com.evo.iam.service.impl.AuthorityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    @Autowired
    private AuthorityServiceImpl authorityService;

    @GetMapping("/{userId}/authority")
    public Response<UserAuthority> getUserAuthority(@PathVariable UUID userId) {
        Response<UserAuthority> response = new Response<UserAuthority>().success();
        response.setData(authorityService.getUserAuthority(userId));
        return response;
    }

    @GetMapping("/{username}/authorities-by-username")
    public Response<UserAuthority> getUserAuthorityByUsername(@PathVariable String username) {
        Response<UserAuthority> response = new Response<UserAuthority>().success();
        response.setData(authorityService.getUserAuthority(username));
        return response;
    }
}
