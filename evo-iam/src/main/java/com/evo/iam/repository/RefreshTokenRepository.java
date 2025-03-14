package com.evo.iam.repository;


import com.evo.iam.entity.RefreshToken;
import com.evo.iam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken findByUser(User user);

    Boolean existsByUser(User user);

    @Modifying
    int deleteByUser(User user);
}
