package com.evo.ddd.application.dto.request.identityKeycloak;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CredentialRequest {
    private String type;
    private String value;
    private boolean temporary;
}
