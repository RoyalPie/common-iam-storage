package com.evo.storage.application.service.impl;

import com.evo.common.dto.response.FileResponse;
import com.evo.storage.application.dto.mapper.FileResponseMapper;
import com.evo.storage.application.dto.request.UpdateFileRequest;
import com.evo.storage.application.mapper.CommandMapper;
import com.evo.storage.application.service.FileCommandService;
import com.evo.storage.domain.File;
import com.evo.storage.domain.FileHistory;
import com.evo.storage.domain.command.StoreFileCmd;
import com.evo.storage.domain.command.UpdateFileCmd;
import com.evo.storage.domain.command.WriteHistoryCmd;
import com.evo.storage.domain.repository.FileDomainRepository;
import com.evo.storage.infrastructure.support.exception.AppErrorCode;
import com.evo.storage.infrastructure.support.exception.AppException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileCommandServiceImpl implements FileCommandService {
    private Path publicStorageLocation;
    private Path privateStorageLocation;
    private final FileStorageProperties fileStorageProperties;
    private final FileDomainRepository fileDomainRepository;
    private final FileResponseMapper fileResponseMapper;
    private final CommandMapper commandMapper;


    @Override
    public List<FileResponse> storeFile(List<MultipartFile> files, boolean isPublic, String description) {
        List<File> fileDomains = new ArrayList<>();
        for(MultipartFile file : files) {
            try {
                validateFile(file);
                if (isImage(file)) {
                    BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                }
                StoreFileCmd storeFilecmd = StoreFileCmd.builder()
                        .originName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .isPublic(isPublic)
                        .build();

                File fileDomain = new File(storeFilecmd);
                Path storageLocation = isPublic ? publicStorageLocation : privateStorageLocation;
                Path targetLocation = storageLocation.resolve(fileDomain.getMd5Name());
                file.transferTo(targetLocation.toFile());

                WriteHistoryCmd writeHistoryCmd = WriteHistoryCmd.builder()
                        .fileId(fileDomain.getId())
                        .action("Store file")
                        .build();
                FileHistory fileHistory = new FileHistory(writeHistoryCmd);
                fileDomain.setHistory(fileHistory);
                fileDomains.add(fileDomain);
            } catch (IOException e) {
                throw new AppException(AppErrorCode.CANT_STORE_FILE);
            }
        }

        fileDomains = fileDomainRepository.saveAll(fileDomains);
        return fileResponseMapper.listDomainModelToListDTO(fileDomains);
    }

    @Override
    public FileResponse updateFile(UpdateFileRequest updateFileRequest) {
        File file = fileDomainRepository.getById(updateFileRequest.getFileId());
        UpdateFileCmd updateFileCmd = commandMapper.from(updateFileRequest);
        file.update(updateFileCmd);
        WriteHistoryCmd writeHistoryCmd = WriteHistoryCmd.builder()
                .fileId(file.getId())
                .action("Update file")
                .build();
        FileHistory fileHistory = new FileHistory(writeHistoryCmd);
        file.setHistory(fileHistory);
        return fileResponseMapper.domainModelToDTO(fileDomainRepository.save(file));
    }

    @Override
    public void deleteFile(UUID fileId) {
        File file = fileDomainRepository.getById(fileId);
        file.setDeleted(true);
        WriteHistoryCmd writeHistoryCmd = WriteHistoryCmd.builder()
                .fileId(file.getId())
                .action("Delete file")
                .build();
        FileHistory fileHistory = new FileHistory(writeHistoryCmd);
        file.setHistory(fileHistory);
        fileDomainRepository.save(file);
    }

    public void validateFile(MultipartFile file) {
        List<String> allowedMimeTypes = Arrays.asList(fileStorageProperties.getAllowedTypes().split(","));
        List<String> allowedExtensions = Arrays.asList(fileStorageProperties.getAllowedExtensions().split(","));

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String fileExtension = StringUtils.getFilenameExtension(fileName);

        if (contentType == null || !allowedMimeTypes.contains(contentType)) {
            throw new AppException(AppErrorCode.FILE_TYPE_NOT_ALLOWED);
        }

        if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
            throw new AppException(AppErrorCode.FILE_EXTENSION_NOT_ALLOWED);
        }
    }


    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image");
    }
}
