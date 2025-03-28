package com.evo.storage.domain.repository;

import com.evo.common.domainRepository.DomainRepository;
import com.evo.storage.domain.File;
import com.evo.storage.domain.query.SearchFileQuery;

import java.util.List;
import java.util.UUID;

public interface FileDomainRepository extends DomainRepository<File, UUID> {
    List<File> search(SearchFileQuery searchFileQuery);
    Long count(SearchFileQuery searchFileQuery);
}
