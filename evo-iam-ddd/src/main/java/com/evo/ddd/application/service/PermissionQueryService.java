package com.evo.ddd.application.service;

import com.evo.ddd.application.dto.request.SearchPermissionRequest;
import com.evo.ddd.application.dto.response.PermissionDTO;

import java.util.List;

public interface PermissionQueryService {
    List<PermissionDTO> search(SearchPermissionRequest searchPermissionRequest);
    Long totalPermissions(SearchPermissionRequest searchPermissionRequest);
}
