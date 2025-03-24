package com.evo.ddd.domain.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserQuery extends PagingQuery {
    private String keyword;
}
