package com.evo.storage.application.service;

import com.evo.common.dto.response.FileResponse;
import com.evo.common.dto.response.PageApiResponse;
import com.evo.storage.application.dto.request.SearchFileRequest;

import java.util.List;
import java.util.UUID;

public interface FileQueryService {
    PageApiResponse<List<FileResponse>> search(SearchFileRequest searchFileRequest);
    FileResponse getPrivateFile(UUID filedId);
    FileResponse getPublicFile(UUID filedId);
}
