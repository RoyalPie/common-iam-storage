package com.evo.ddd.application.dto.request.identityKeycloak;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RefreshTokenRequest {
    private String client_id;
    private String client_secret;
    private String grant_type;
    private String refresh_token;
}