package com.evo.ddd.domain.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(value = {"type", "temporary"}, allowSetters = true)
public class ResetKeycloakPasswordCmd {
    private String type = "password";
    private String value;
    private boolean temporary = false;
}
