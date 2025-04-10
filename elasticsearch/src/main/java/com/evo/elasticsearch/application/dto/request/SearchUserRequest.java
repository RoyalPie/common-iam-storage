package com.evo.elasticsearch.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserRequest {
    private String keyword;
    private Boolean active;
    private int page;
    private int size;
    private String sortField;
    private String sortDirection;
    private Integer minExperience;
    private Integer maxExperience;
}
