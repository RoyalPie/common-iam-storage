package com.evo.ddd.infrastructure.adapter.storage;

import com.evo.common.client.storage.StorageClient;
import com.evo.common.dto.response.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final StorageClient storageClient;
    private  final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public List<FileResponse> uploadFile(List<MultipartFile> files) {
        return storageClient.uploadPriavteFiles(files).getData();
    }

    @Override
    public void kafkaUloadFile() {
        kafkaTemplate.send("avatar_upload","Test!!!!");
    }


//    @Override
//    public FileResponse getFile(UUID fileId) {
//        return storageClient.getFile(fileId).getData();
//    }
//
//    @Override
//    public FileResponse updateFile(UpdateFileRequest updateFileRequest) {
//        return storageClient.updateFile(updateFileRequest).getData();
//    }
//
//    @Override
//    public void deleteFile(UUID fileId) {
//        storageClient.deleteFile(fileId);
//    }
//
//    @Override
//    public List<FileResponse> search(SearchFileRequest searchFileRequest){
//        return storageClient.searchFiles(searchFileRequest).getData();
//    }
//
//    @Override
//    public ApiResponses<Void> testRetry() {
//        return storageClient.testRetry();
//    }
}
