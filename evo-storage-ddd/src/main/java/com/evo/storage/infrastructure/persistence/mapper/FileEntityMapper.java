package com.evo.storage.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import com.evo.storage.domain.File;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface FileEntityMapper extends EntityMapper<File, FileEntity> {
}
