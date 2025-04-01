package com.evo.storage.presentation;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import com.evo.storage.application.dto.request.SearchFileRequest;
import com.evo.storage.application.dto.request.UpdateFileRequest;
import com.evo.storage.application.service.FileCommandService;
import com.evo.storage.application.service.FileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PrivateFileController {
    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;

    @PreAuthorize("hasPermission(null, 'file.create')")
    @PostMapping("/file/upload")
    public ApiResponses<List<FileResponse>> storeFile(@RequestPart List<MultipartFile> files) {
        List<FileResponse> fileResponses = fileCommandService.storeFile(files, false);
        return ApiResponses.<List<FileResponse>>builder()
                .data(fileResponses)
                .success(true)
                .code(201)
                .message("Files stored successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @PatchMapping("/file/upload")
    public ApiResponses<List<FileResponse>> storeAvatar(@RequestPart List<MultipartFile> files) {
        List<FileResponse> fileResponses = fileCommandService.storeFile(files, false);
        return ApiResponses.<List<FileResponse>>builder()
                .data(fileResponses)
                .success(true)
                .code(201)
                .message("Files stored successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'file.read')")
    @GetMapping("/{filedId}")
    public ApiResponses<FileResponse> getFile(@PathVariable UUID filedId) {
        FileResponse fileResponse = fileQueryService.getPrivateFile(filedId);
        return ApiResponses.<FileResponse>builder()
                .data(fileResponse)
                .success(true)
                .code(200)
                .message("File retrieved successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'file.admin')")
    @GetMapping("")
    public ApiResponses<List<FileResponse>> searchFiles(@RequestBody SearchFileRequest searchFileRequest) {
        return fileQueryService.search(searchFileRequest);
    }

    @PreAuthorize("hasPermission(null, 'file.update')")
    @PutMapping("/update")
    public ApiResponses<FileResponse> updateFile(@RequestBody UpdateFileRequest updateFileRequest) {
        FileResponse fileResponse = fileCommandService.updateFile(updateFileRequest);
        return ApiResponses.<FileResponse>builder()
                .data(fileResponse)
                .success(true)
                .code(200)
                .message("File updated successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }

    @PreAuthorize("hasPermission(null, 'file.delete')")
    @DeleteMapping("/delete/{fileId}")
    public ApiResponses<Void> deleteFile(@PathVariable UUID fileId) {
        fileCommandService.deleteFile(fileId);
        return ApiResponses.<Void>builder()
                .success(true)
                .code(204)
                .message("File deleted successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
}