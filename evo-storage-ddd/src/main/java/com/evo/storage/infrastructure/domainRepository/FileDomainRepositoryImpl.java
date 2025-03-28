package com.evo.storage.infrastructure.domainRepository;

import com.evo.common.domainRepository.AbstractDomainRepository;
import com.evo.storage.domain.File;
import com.evo.storage.domain.FileHistory;
import com.evo.storage.domain.query.SearchFileQuery;
import com.evo.storage.domain.repository.FileDomainRepository;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import com.evo.storage.infrastructure.persistence.entity.FileHistoryEntity;
import com.evo.storage.infrastructure.persistence.mapper.FileEntityMapper;
import com.evo.storage.infrastructure.persistence.mapper.FileHistoryEntityMapper;
import com.evo.storage.infrastructure.persistence.repository.FileEntityRepository;
import com.evo.storage.infrastructure.persistence.repository.FileHistoryEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FileDomainRepositoryImpl extends AbstractDomainRepository<File, FileEntity, UUID>
    implements FileDomainRepository {
    private final FileEntityMapper fileEntityMapper;
    private final FileEntityRepository fileEntityRepository;
    private final FileHistoryEntityMapper fileHistoryEntityMapper;
    private final FileHistoryEntityRepository fileHistoryEntityRepository;

    public FileDomainRepositoryImpl(FileEntityRepository fileEntityRepository,
                                    FileEntityMapper fileEntityMapper,
                                    FileHistoryEntityMapper fileHistoryEntityMapper,
                                    FileHistoryEntityRepository fileHistoryEntityRepository) {
        super(fileEntityRepository, fileEntityMapper);
        this.fileEntityRepository = fileEntityRepository;
        this.fileEntityMapper = fileEntityMapper;
        this.fileHistoryEntityMapper = fileHistoryEntityMapper;
        this.fileHistoryEntityRepository = fileHistoryEntityRepository;
    }

    @Override
    public List<File> search(SearchFileQuery searchFileQuery) {
        List<FileEntity> fileEntities = fileEntityRepository.search(searchFileQuery);
        return fileEntityMapper.toDomainModelList(fileEntities);
    }

    @Override
    public Long count(SearchFileQuery searchFileQuery) {
        return fileEntityRepository.count(searchFileQuery);
    }

    @Override
    public List<File> saveAll(List<File> domains) {
        List<FileHistory> fileHistories = domains.stream()
                .map(File::getHistory)
                .toList();
        List<FileEntity> fileEntities = fileEntityMapper.toEntityList(domains);
        List<FileHistoryEntity> fileHistoryEntities = fileHistoryEntityMapper.toEntityList(fileHistories);
        fileHistoryEntityRepository.saveAll(fileHistoryEntities);
        return fileEntityMapper.toDomainModelList(fileEntityRepository.saveAll(fileEntities));
    }

    @Override
    public File getById(UUID uuid) {
        return entityMapper.toDomainModel(repository.findById(uuid).orElseThrow(() -> new RuntimeException("File not found")));
    }
}
