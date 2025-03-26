package com.evo.ddd.application.service.impl.query;

import com.evo.common.UserAuthority;
import com.evo.common.webapp.security.AuthorityService;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.Role;
import com.evo.ddd.domain.User;
import com.evo.ddd.domain.UserRole;
import com.evo.ddd.domain.repository.RoleDomainRepository;
import com.evo.ddd.domain.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class AuthorityQueryServiceImpl implements AuthorityService {

    private final RoleDomainRepository roleDomainRepository;
    private final UserDomainRepository userDomainRepository;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        return null;
    }

    @Override
    public UserAuthority getUserAuthority(String username) {
        User user = userDomainRepository.getByUsername(username);
        UserRole userRole = user.getUserRole();
        Role role = roleDomainRepository.getById(userRole.getRoleId());
        boolean isRoot = role.isRoot();
        List<Permission> permissions = roleDomainRepository.findPermissionByRoleId(userRole.getRoleId());
        List<String> grantedPermissions = List.of();
        if (permissions != null && !permissions.isEmpty()) {
            grantedPermissions = permissions.stream()
                    .filter(Objects::nonNull) // Lọc các phần tử null nếu có
                    .map(permission -> permission.getResource() + "." + permission.getScope())
                    .toList();
        }
        return UserAuthority.builder()
                .userId(user.getUserID())
                .isRoot(isRoot)
                .grantedPermissions(grantedPermissions)
                .build();
    }

    @Override
    public UserAuthority getClientAuthority(UUID clientId) {
        return null;
    }
}
