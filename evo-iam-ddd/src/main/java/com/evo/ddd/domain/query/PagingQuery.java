package com.evo.ddd.domain.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingQuery {
    private int pageIndex;
    private int pageSize;
    private String sortBy;
}
