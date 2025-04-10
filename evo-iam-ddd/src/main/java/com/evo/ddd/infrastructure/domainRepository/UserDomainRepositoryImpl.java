package com.evo.ddd.infrastructure.domainRepository;

import com.evo.ddd.domain.User;
import com.evo.ddd.domain.UserActivityLog;
import com.evo.ddd.domain.UserRole;
import com.evo.ddd.domain.query.SearchUserQuery;
import com.evo.ddd.domain.repository.UserDomainRepository;
import com.evo.ddd.infrastructure.persistence.entity.UserActivityLogEntity;
import com.evo.ddd.infrastructure.persistence.entity.UserEntity;
import com.evo.common.domainRepository.AbstractDomainRepository;
import com.evo.ddd.infrastructure.persistence.entity.UserRoleEntity;
import com.evo.ddd.infrastructure.persistence.mapper.UserActivityLogEntityMapper;
import com.evo.ddd.infrastructure.persistence.mapper.UserEntityMapper;
import com.evo.ddd.infrastructure.persistence.mapper.UserRoleEntityMapper;
import com.evo.ddd.infrastructure.persistence.repository.UserActivityLogEntityRepository;
import com.evo.ddd.infrastructure.persistence.repository.UserEntityRepository;
import com.evo.ddd.infrastructure.persistence.repository.UserRoleEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserDomainRepositoryImpl extends AbstractDomainRepository<User, UserEntity, UUID>
        implements UserDomainRepository {
    private final UserEntityMapper userEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final UserRoleEntityMapper userRoleEntityMapper;
    private final UserActivityLogEntityRepository userActivityLogEntityRepository;
    private final UserActivityLogEntityMapper userActivityLogEntityMapper;

    public UserDomainRepositoryImpl(UserEntityRepository userEntityRepository,
                                    UserEntityMapper userEntityMapper,
                                    UserRoleEntityRepository userRoleEntityRepository,
                                    UserRoleEntityMapper userRoleEntityMapper,
                                    UserActivityLogEntityRepository userActivityLogEntityRepository,
                                    UserActivityLogEntityMapper userActivityLogEntityMapper) {
        super(userEntityRepository, userEntityMapper);
        this.userEntityRepository = userEntityRepository;
        this.userEntityMapper = userEntityMapper;
        this.userRoleEntityRepository = userRoleEntityRepository;
        this.userRoleEntityMapper = userRoleEntityMapper;
        this.userActivityLogEntityRepository = userActivityLogEntityRepository;
        this.userActivityLogEntityMapper = userActivityLogEntityMapper;
    }

    @Override
    public List<User> search(SearchUserQuery query) {
        List<UserEntity> userEntities = userEntityRepository.search(query);
        return this.enrichList(userEntityMapper.toDomainModelList(userEntities));
    }

    @Override
    @Transactional
    public User save(User domainModel) {
        UserEntity userEntity = userEntityMapper.toEntity(domainModel);
        userEntity = userEntityRepository.save(userEntity);

        List<UserRoleEntity> userRoleEntities = domainModel.getUserRole().stream()
                .map(userRoleEntityMapper::toEntity)
                .toList();
        userRoleEntityRepository.saveAll(userRoleEntities); // Save all roles at once

        UserActivityLogEntity userActivityLogEntity = userActivityLogEntityMapper.toEntity(domainModel.getUserActivityLog());
        userActivityLogEntityRepository.save(userActivityLogEntity);

        return userEntityMapper.toDomainModel(userEntity);
    }

    @Override
    public User getById(UUID uuid) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return this.enrich(userEntityMapper.toDomainModel(userEntity));
    }

    @Override
    public List<User> getAll() {
        List<UserEntity> users = userEntityRepository.findAll();
        return this.enrichList(userEntityMapper.toDomainModelList(users));
    }

    @Override
    protected List<User> enrichList(List<User> users) {
        if (users.isEmpty()) return users;

        List<UUID> userIds = users.stream().map(User::getUserID).toList();

        Map<UUID, List<UserRole>> userRoleMap = userRoleEntityRepository.findByUserIdIn(userIds).stream()
                .map(userRoleEntityMapper::toDomainModel)
                .collect(Collectors.groupingBy(UserRole::getUserId));

        users.forEach(user -> user.setUserRole(userRoleMap.getOrDefault(user.getUserID(), new ArrayList<>())));

        return users;
    }


    @Override
    public boolean existsByUsername(String username) {
        return userEntityRepository.existsByUsername(username);
    }

    @Override
    public Long count(SearchUserQuery searchUserQuery) {
        return userEntityRepository.count(searchUserQuery);
    }
}