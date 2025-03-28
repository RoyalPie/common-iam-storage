package com.evo.storage.application.mapper;

import com.evo.storage.application.dto.request.SearchFileRequest;
import com.evo.storage.domain.query.SearchFileQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QueryMapper {
    SearchFileQuery from(SearchFileRequest searchFileRequest);
}
