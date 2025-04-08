package com.evo.elasticsearch.infrastructure.domainRepository;

import java.util.List;

public interface DocumentDomainRepository<D, ID> {
    D save(D document);
    List<D> saveAll(List<D> documents);
    D getById(ID id);
}
