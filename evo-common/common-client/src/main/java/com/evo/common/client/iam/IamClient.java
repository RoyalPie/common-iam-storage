package com.evo.common.client.iam;

import com.evo.common.UserAuthority;
import com.evo.common.config.FeignClientConfiguration;
import com.evo.common.dto.response.Response;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        url = "${app.iam.internal-url:}",
        name = "iam",
        contextId = "common-iam",
        configuration = FeignClientConfiguration.class,
        fallbackFactory = IamClientFallback.class)
public interface IamClient {
    @GetMapping("/api/users/{userId}/authorities")
    @LoadBalanced
    Response<UserAuthority> getUserAuthority(@PathVariable UUID userId);

    @GetMapping("/api/users/{username}/authorities-by-username")
    @LoadBalanced
    Response<UserAuthority> getUserAuthority(@PathVariable String username);

    @PostMapping("/api/users/revoke-refresh-token")
    @LoadBalanced
    Response<?> invalidRefreshToken(@RequestBody String refreshToken);
}
