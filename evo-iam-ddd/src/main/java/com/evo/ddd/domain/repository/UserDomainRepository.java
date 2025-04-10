package com.evo.ddd.domain.repository;

import com.evo.common.domainRepository.DomainRepository;
import com.evo.ddd.domain.User;
import com.evo.ddd.domain.query.SearchUserQuery;

import java.util.List;
import java.util.UUID;

public interface UserDomainRepository extends DomainRepository<User, UUID> {
    List<User> search(SearchUserQuery query);
    User getByUsername(String username);
    List<User> getAll();
    boolean existsByUsername(String username);
    Long count(SearchUserQuery searchUserQuery);
}
