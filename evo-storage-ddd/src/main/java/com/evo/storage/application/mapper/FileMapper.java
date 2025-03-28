package com.evo.storage.application.mapper;

import com.evo.common.dto.response.FileResponse;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileResponse fileToFileResponse(FileEntity fileEntity);
}
