package com.evo.elasticsearch.infrastructure.persistence.mapper;

import com.evo.elasticsearch.domain.User;
import com.evo.elasticsearch.infrastructure.persistence.document.UserDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDocumentMapper extends DocumentMapper<User, UserDocument> {}