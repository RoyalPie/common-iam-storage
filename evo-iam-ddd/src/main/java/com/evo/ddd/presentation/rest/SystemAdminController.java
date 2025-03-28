package com.evo.ddd.presentation.rest;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.PageApiResponse;
import com.evo.ddd.application.dto.request.CreateOrUpdatePermissionRequest;
import com.evo.ddd.application.dto.request.CreateOrUpdateRoleRequest;
import com.evo.ddd.application.dto.request.SearchPermissionRequest;
import com.evo.ddd.application.dto.response.PermissionDTO;
import com.evo.ddd.application.dto.response.RoleDTO;
import com.evo.ddd.application.service.PermissionCommandService;
import com.evo.ddd.application.service.PermissionQueryService;
import com.evo.ddd.application.service.RoleCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class SystemAdminController {
    private final PermissionCommandService permissionCommandService;
    private final PermissionQueryService permissionQueryService;
    private final RoleCommandService roleCommandService;

    @PostMapping("/permissions")
    public ApiResponses<PermissionDTO> createPermission(@RequestBody CreateOrUpdatePermissionRequest createOrUpdatePermissionRequest) {
        PermissionDTO permissionDTO = permissionCommandService.createPermission(createOrUpdatePermissionRequest);
        return ApiResponses.<PermissionDTO>builder()
                .data(permissionDTO)
                .success(true)
                .code(201)
                .message("Permission created successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PostMapping("/permissions/search")
    PageApiResponse<List<PermissionDTO>> search(@RequestBody SearchPermissionRequest searchPermissionRequest) {
        Long totalPermissions = permissionQueryService.totalPermissions(searchPermissionRequest);
        List<PermissionDTO> permissionDTOs = Collections.emptyList();
        if(totalPermissions != 0) {
            permissionDTOs = permissionQueryService.search(searchPermissionRequest);
        }
        PageApiResponse.PageableResponse pageableResponse = PageApiResponse.PageableResponse.builder()
                .pageSize(searchPermissionRequest.getPageSize())
                .pageIndex(searchPermissionRequest.getPageIndex())
                .totalElements(totalPermissions)
                .totalPages((int)(Math.ceil((double)totalPermissions / searchPermissionRequest.getPageSize())))
                .hasNext(searchPermissionRequest.getPageIndex() + searchPermissionRequest.getPageSize() < totalPermissions)
                .hasPrevious(searchPermissionRequest.getPageIndex() > 1).build();

        return PageApiResponse.<List<PermissionDTO>>builder()
                .data(permissionDTOs)
                .pageable(pageableResponse)
                .success(true)
                .code(200)
                .message("Search permission successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PostMapping("/roles")
    public ApiResponses<RoleDTO> createRole(@RequestBody CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        RoleDTO RoleDTO =  roleCommandService.createRole(createOrUpdateRoleRequest);
        return ApiResponses.<RoleDTO>builder()
                .data(RoleDTO)
                .success(true)
                .code(201)
                .message("Role created successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PutMapping("/roles")
    public ApiResponses<RoleDTO> updateRole(@RequestBody CreateOrUpdateRoleRequest createOrUpdateRoleRequest) {
        RoleDTO RoleDTO =  roleCommandService.updateRole(createOrUpdateRoleRequest);
        return ApiResponses.<RoleDTO>builder()
                .data(RoleDTO)
                .success(true)
                .code(200)
                .message("Role updated successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}
