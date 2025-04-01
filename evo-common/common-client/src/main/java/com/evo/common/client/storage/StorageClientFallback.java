package com.evo.common.client.storage;

import com.evo.common.dto.response.ApiResponses;
import com.evo.common.dto.response.FileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class StorageClientFallback implements FallbackFactory<StorageClient> {
    @Override
    public StorageClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    @Slf4j
    static class FallbackWithFactory implements StorageClient {
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }


        @Override
        public List<FileResponse> uploadPublicFiles(MultipartFile[] files) {
            return null;
        }

        @Override
        public ApiResponses<List<FileResponse>> uploadPriavteFiles(List<MultipartFile> files) {
            return null;
        }

        @Override
        public List<FileResponse> uploadAvatar(List<MultipartFile> files) {
            return List.of();
        }
    }
}
