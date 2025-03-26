package com.evo.ddd.application.dto.mapper;

public interface DTOMapper<D, M, E> {
    D domainModelToDTO(M model);
    M dtoToDomainModel(D dto);
    E domainModelToEntity(M model);
    M entityToDomainModel(E entity);
}
