package com.evo.ddd.application.mapper;

import com.evo.ddd.application.dto.request.SearchPermissionRequest;
import com.evo.ddd.application.dto.request.SearchUserRequest;
import com.evo.ddd.domain.query.SearchPermissionQuery;
import com.evo.ddd.domain.query.SearchUserQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QueryMapper {
    SearchPermissionQuery from(SearchPermissionRequest request);
    SearchUserQuery from(SearchUserRequest request);
}
