package com.evo.elasticsearch.domain.repository;

import com.evo.elasticsearch.domain.User;
import com.evo.elasticsearch.infrastructure.domainRepository.DocumentDomainRepository;

import java.util.UUID;

public interface UserDomainRepository extends DocumentDomainRepository<User, UUID> {
    void deleteById(UUID userId);
}
