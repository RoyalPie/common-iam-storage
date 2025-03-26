package com.evo.ddd.application.service.impl.query;

import com.evo.common.UserAuthority;

import com.evo.ddd.application.dto.mapper.UserDTOMapper;
import com.evo.ddd.application.dto.request.SearchUserRequest;
import com.evo.ddd.application.dto.response.UserDTO;
import com.evo.ddd.application.mapper.QueryMapper;
import com.evo.ddd.application.service.UserQueryService;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.Role;
import com.evo.ddd.domain.User;
import com.evo.ddd.domain.UserRole;
import com.evo.ddd.domain.query.SearchUserQuery;
import com.evo.ddd.domain.repository.RoleDomainRepository;
import com.evo.ddd.domain.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserDomainRepository userDomainRepository;
    private final UserDTOMapper userDTOMapper;
    private final QueryMapper queryMapper;
    private final RoleDomainRepository roleDomainRepository;


    @Override
    public UserDTO getUserInfo(String username) {
        User user = userDomainRepository.getByUsername(username);
        return userDTOMapper.domainModelToDTO(user);
    }

    @Override
    public Long totalUsers(SearchUserRequest request) {
        SearchUserQuery searchUserQuery = queryMapper.from(request);
        return userDomainRepository.count(searchUserQuery);
    }

    @Override
    public List<UserDTO> search(SearchUserRequest request) {
        SearchUserQuery searchUserQuery = queryMapper.from(request);
        List<User> users = userDomainRepository.search(searchUserQuery);
        return users.stream().map(userDTOMapper::domainModelToDTO).toList();
    }

//    @Override
//    public void exportUserListToExcel(SearchUserRequest searchUserRequest) {
//        List<UserDTO> userDTOS = search(searchUserRequest);
//        List<User> users = userDTOS.stream().map(userDTOMapper::dtoToDomainModel).toList();
//        Map<String, Object> data = new HashMap<>();
//        data.put("users", users);
//        try (InputStream inputStream = new ClassPathResource("templates/user_template.xlsx").getInputStream()) {
//            JxlsOutput output = new JxlsOutputFile(new File("exports/output_user_data.xlsx"));
//            JxlsPoiTemplateFillerBuilder.newInstance()
//                    .withTemplate(inputStream)
//                    .build()
//                    .fill(data, output);
//        } catch (IOException e) {
//            throw new AppException(AppErrorCode.FILE_NOT_FOUND);
//        }
//    }

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
        return UserAuthority.builder().userId(user.getUserID()).isRoot(isRoot).grantedPermissions(grantedPermissions).build();
    }

//    @Override
//    public UserAuthority getClientAuthority(String clientId) {
//        OauthClient oauthClient = oauthClientDomainRepository.findByClientId(clientId);
//        return UserAuthority.builder().userId(oauthClient.getId()).isRoot(false).isClient(true).build();
//    }
}
