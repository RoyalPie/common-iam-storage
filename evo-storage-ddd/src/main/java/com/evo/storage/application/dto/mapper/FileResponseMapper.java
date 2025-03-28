package com.evo.storage.application.dto.mapper;

import com.evo.common.dto.response.FileResponse;
import com.evo.common.mapper.DTOMapper;
import com.evo.storage.domain.File;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileResponseMapper extends DTOMapper<FileResponse, File, FileEntity> {
    List<FileResponse> listDomainModelToListDTO(List<File> domainModel);
}
