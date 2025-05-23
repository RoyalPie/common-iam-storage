package com.evo.elasticsearch.presentation.rest;

import com.evo.common.dto.response.PageApiResponse;
import com.evo.elasticsearch.application.dto.request.SearchUserRequest;
import com.evo.elasticsearch.application.dto.response.SearchUserResponse;
import com.evo.elasticsearch.application.service.UserQueryService;
import com.evo.elasticsearch.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/")
public class SearchController {
    private final UserQueryService userQueryService;

    @PreAuthorize("hasPermission('user', 'admin')")
    @GetMapping("/user")
    PageApiResponse<List<User>> searchUser(@RequestBody SearchUserRequest request) {
        SearchUserResponse searchUserResponse = userQueryService.searchUser(request);
        PageApiResponse.PageableResponse pageableResponse = PageApiResponse.PageableResponse.builder()
                .pageIndex(searchUserResponse.getPageIndex())
                .totalPages(searchUserResponse.getTotalPages())
                .totalElements(searchUserResponse.getTotalElements())
                .pageSize(searchUserResponse.getPageSize())
                .hasPrevious(searchUserResponse.isHasPrevious())
                .hasNext(searchUserResponse.isHasNext())
                .build();

        return PageApiResponse.<List<User>>builder()
                .data(searchUserResponse.getUsers())
                .pageable(pageableResponse)
                .success(true)
                .code(200)
                .message("Search user successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();

    }
}
