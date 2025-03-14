package com.evo.iam.service.impl;

import com.evo.common.UserAuthority;
import com.evo.common.webapp.security.AuthorityService;
import com.evo.iam.entity.Role;
import com.evo.iam.entity.User;
import com.evo.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserAuthority getUserAuthority(UUID userId) { return null; }

    @Override
    public UserAuthority getUserAuthority(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found with email "+username));

        return UserAuthority.builder()
                .userId(user.getId())
                .isRoot(user.getRoles().stream().anyMatch(Role::getIsRoot))
                .grantedPermissions(user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(permission -> permission.getResource()+"."+permission.getPermission())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserAuthority getClientAuthority(UUID clientId) {
        return null;
    }
}
