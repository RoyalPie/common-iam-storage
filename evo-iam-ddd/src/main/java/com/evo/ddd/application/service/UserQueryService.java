package com.evo.ddd.application.service;

import com.evo.common.UserAuthority;
import com.evo.ddd.application.dto.request.SearchUserRequest;
import com.evo.ddd.application.dto.response.UserDTO;

import java.util.List;

public interface UserQueryService {
    UserDTO getUserInfo(String username);
    Long totalUsers(SearchUserRequest request);
    List<UserDTO> search(SearchUserRequest searchUserRequest);
//    void exportUserListToExcel(SearchUserRequest searchUserRequest);
    UserAuthority getUserAuthority(String username);
//    UserAuthority getClientAuthority(String clientId);
}
