package com.evo.ddd.presentation.rest;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.PageApiResponse;
import com.evo.ddd.application.dto.request.SearchUserRequest;
import com.evo.ddd.application.dto.response.UserDTO;
import com.evo.ddd.application.service.UserCommandService;
import com.evo.ddd.application.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-manager")
public class ManagerController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @GetMapping("/users/search")
    public PageApiResponse<List<UserDTO>> search(@RequestBody SearchUserRequest searchUserRequest) {
        Long totalUsers = userQueryService.totalUsers(searchUserRequest);
        List<UserDTO> userDTOS = Collections.emptyList();
        if(totalUsers != 0) {
            userDTOS = userQueryService.search(searchUserRequest);
        }
        PageApiResponse.PageableResponse pageableResponse = PageApiResponse.PageableResponse.builder()
                .pageSize(searchUserRequest.getPageSize())
                .pageIndex(searchUserRequest.getPageIndex())
                .totalElements(totalUsers)
                .totalPages((int)(Math.ceil((double)totalUsers / searchUserRequest.getPageSize())))
                .hasNext(searchUserRequest.getPageIndex() + searchUserRequest.getPageSize() < totalUsers)
                .hasPrevious(searchUserRequest.getPageIndex() > 1).build();

        return PageApiResponse.<List<UserDTO>>builder()
                .data(userDTOS)
                .pageable(pageableResponse)
                .success(true)
                .code(200)
                .message("Search user successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }


    @PatchMapping("/users/lock")
    public ApiResponses<Void> lockUser(@Parameter(description = "ID của người dùng cần cập nhật trạng thái", example = "12345")
                                       @RequestParam String username,
                                       @Parameter(description = "Trạng thái của người dùng: `true` để khoá, `false` để mở khoá", example = "true")
                                       @RequestParam boolean enabled) {
        userCommandService.lockUser(username, enabled);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(200)
                .message("Lock/Unlock user successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
