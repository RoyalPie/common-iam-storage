package com.evo.storage.infrastructure.persistence.repository.custom;

import com.evo.storage.domain.query.SearchFileQuery;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;

import java.util.List;

public interface FileEntityRepositoryCustom {
    List<FileEntity> search(SearchFileQuery searchFileQuery);
    Long count(SearchFileQuery searchFileQuery);
}
