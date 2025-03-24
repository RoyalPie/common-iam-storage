package com.evo.ddd.infrastructure.persistence.repository.custom;

import com.evo.ddd.domain.query.SearchUserQuery;
import com.evo.ddd.infrastructure.persistence.entity.UserEntity;

import java.util.List;

public interface UserEntityRepositoryCustom {
    List<UserEntity> search(SearchUserQuery searchUserQuery);
    Long count(SearchUserQuery searchUserQuery);
}
