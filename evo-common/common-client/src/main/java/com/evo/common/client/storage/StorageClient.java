package com.evo.common.client.storage;

import com.evo.common.config.StorageClientConfiguration;
import com.evo.common.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(
        url = "${app.storage.internal-url:}",
        name = "storage",
        contextId = "common-storage",
        configuration = StorageClientConfiguration.class,
        fallbackFactory = StorageClientFallback.class)
public interface StorageClient {
    @PostMapping(value = "/api/public/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<FileResponse> uploadPublicFiles(@RequestPart("file") MultipartFile[] files);

    @PostMapping(value = "/api/private/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<FileResponse> uploadPriavteFiles(@RequestPart("file") MultipartFile[] files);

//    @PutMapping(value = "/api/file/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    Response<FileResponse> updateFile(@RequestBody UpdateFileRequest updateFileRequest);

//    @DeleteMapping("/api/files/delete/{fileId}")
//    Response<Void> deleteFile(@PathVariable("fileId") UUID fileId);
//
//    @GetMapping("/api/files/{fileId}")
//    Response<FileResponse> getFile(@PathVariable("fileId") UUID fileId);
//
//    @PostMapping(value = "/api/files")
//    PagingResponse<List<FileResponse>> searchFiles(@RequestParam(required = false) String extensionType,
//                                                   @RequestParam(required = false) String ownerId,
//                                                   @RequestParam(required = false) String dateFilterMode,
//                                                   @RequestParam(required = false) String filterDate,
//                                                   @RequestParam(defaultValue = "0") int page,
//                                                   @RequestParam(defaultValue = "10") int size,
//                                                   @RequestParam(defaultValue = "createdDate,desc") String sortValues);
}
