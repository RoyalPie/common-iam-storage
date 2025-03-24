package com.evo.common.client.storage;

import com.evo.common.UserAuthority;
import com.evo.common.client.iam.IamClient;
import com.evo.common.dto.FileResponse;
import com.evo.common.dto.response.Response;
import com.evo.common.enums.ServiceUnavailableError;
import com.evo.common.exception.ForwardInnerAlertException;
import com.evo.common.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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
        public List<FileResponse> uploadPriavteFiles(MultipartFile[] files) {
            return List.of();
        }
    }
}
