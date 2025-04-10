package com.evo.elasticsearch.application.service;

import com.evo.elasticsearch.application.dto.request.SearchUserRequest;
import com.evo.elasticsearch.application.dto.response.SearchUserResponse;

public interface UserQueryService {
    SearchUserResponse searchUser(SearchUserRequest request);
}
