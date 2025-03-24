package com.evo.ddd.domain.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPermissionQuery extends PagingQuery {
    private String keyword;
}
