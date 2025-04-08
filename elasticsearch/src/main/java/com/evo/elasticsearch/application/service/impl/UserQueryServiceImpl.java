package com.evo.elasticsearch.application.service.impl;

import com.evo.elasticsearch.application.dto.request.SearchUserRequest;
import com.evo.elasticsearch.application.dto.response.SearchUserResponse;
import com.evo.elasticsearch.application.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    @Override
    public SearchUserResponse searchUser(SearchUserRequest request) {
        return null;
    }
}
