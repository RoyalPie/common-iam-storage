package com.evo.storage.application.mapper;

import com.evo.storage.application.dto.request.UpdateFileRequest;
import com.evo.storage.domain.command.UpdateFileCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    UpdateFileCmd from(UpdateFileRequest request);
}
