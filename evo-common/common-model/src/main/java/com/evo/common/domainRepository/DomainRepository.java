package com.evo.common.domainRepository;

import java.util.List;

public interface DomainRepository<D, ID> {
    D save(D entity);
    List<D> saveAll(List<D> entities);
    List<D> findAllByIds(List<ID> ids);
    D getById(ID id);
}