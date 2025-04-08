package com.evo.elasticsearch.infrastructure.domainRepository;

import com.evo.elasticsearch.infrastructure.persistence.mapper.DocumentMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public abstract class AbtractDocumentRepository<D, E, ID> implements DocumentDomainRepository<D, ID> {
    protected final ElasticsearchRepository<E, ID> repository;
    protected final DocumentMapper<D, E> mapper;

    protected AbtractDocumentRepository(ElasticsearchRepository<E, ID> repository, DocumentMapper<D, E> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    @Override
    @Transactional
    public D save(D domainModel) {
        List<D> domainModels = this.saveAll(List.of(domainModel));
        return domainModels.getFirst();
    }

    @Override
    @Transactional
    public List<D> saveAll(List<D> domains) {
        Iterable<E> documents = this.mapper.toDocumentList(domains);
        documents = this.repository.saveAll(documents);
        return this.mapper.toDomainModelList(documents);
    }
}
