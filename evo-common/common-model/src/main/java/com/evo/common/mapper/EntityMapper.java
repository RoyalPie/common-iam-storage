package com.evo.common.mapper;

import java.util.List;

public interface EntityMapper<D, E>{
    D toDomainModel(E entity);
    E toEntity(D domainModel);
    List<D> toDomainModelList(List<E> entityList);
    List<E> toEntityList(List<D> domainModelList);
}
