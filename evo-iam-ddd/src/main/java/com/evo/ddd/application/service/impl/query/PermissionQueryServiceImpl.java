package com.evo.ddd.application.service.impl.query;

import com.evo.ddd.application.dto.mapper.PermissionDTOMapper;
import com.evo.ddd.application.dto.request.SearchPermissionRequest;
import com.evo.ddd.application.dto.response.PermissionDTO;
import com.evo.ddd.application.mapper.QueryMapper;
import com.evo.ddd.application.service.PermissionQueryService;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.query.SearchPermissionQuery;
import com.evo.ddd.domain.repository.PermissionDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionQueryServiceImpl implements PermissionQueryService {
    private final PermissionDomainRepository permissionDomainRepository;
    private final QueryMapper queryMapper;
    private final PermissionDTOMapper permissionDTOMapper;

    @Override
    public List<PermissionDTO> search(SearchPermissionRequest searchPermissionRequest) {
        SearchPermissionQuery searchPermissionQuery = queryMapper.from(searchPermissionRequest);
        List<Permission> permissions = permissionDomainRepository.search(searchPermissionQuery);
        return permissions.stream().map(permissionDTOMapper::domainModelToDTO).toList();

    }

    @Override
    public Long totalPermissions(SearchPermissionRequest searchPermissionRequest) {
        SearchPermissionQuery searchPermissionQuery = queryMapper.from(searchPermissionRequest);
        return permissionDomainRepository.count(searchPermissionQuery);
    }
}
