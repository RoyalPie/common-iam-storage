package com.evo.storage.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import com.evo.storage.domain.FileHistory;
import com.evo.storage.infrastructure.persistence.entity.FileHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileHistoryEntityMapper extends EntityMapper<FileHistory, FileHistoryEntity> {
}
