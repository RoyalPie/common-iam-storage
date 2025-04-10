package com.evo.elasticsearch.application.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.evo.elasticsearch.application.dto.request.SearchUserRequest;
import com.evo.elasticsearch.application.dto.response.SearchUserResponse;
import com.evo.elasticsearch.application.service.UserQueryService;
import com.evo.elasticsearch.infrastructure.persistence.document.UserDocument;
import com.evo.elasticsearch.infrastructure.persistence.mapper.UserDocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final UserDocumentMapper userDocumentMapper;

    @Override
    public SearchUserResponse searchUser(SearchUserRequest request) {
        Query query = Query.of(q -> q
                .bool(b -> b
                        .should(s -> s.matchPhrasePrefix(m -> m
                                .field("username")
                                .query(request.getKeyword())
                        ))
                        .should(s -> s.match(m -> m
                                .field("username")
                                .query(request.getKeyword())
                                .fuzziness("AUTO")
                        ))
                )
        );
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(PageRequest.of(
                        request.getPage(),
                        request.getSize(),
                        Sort.by(
                                request.getSortDirection().equalsIgnoreCase("desc")
                                        ? Sort.Direction.DESC
                                        : Sort.Direction.ASC,
                                request.getSortField())))
                .build();
        SearchHits<UserDocument> searchHits = elasticsearchOperations.search(searchQuery, UserDocument.class);

        List<UserDocument> userDocuments =
                searchHits.getSearchHits().stream().map(SearchHit::getContent).toList();

        return SearchUserResponse.builder()
                .users(userDocumentMapper.toDomainModelList(userDocuments))
                .totalElements(searchHits.getTotalHits())
                .totalPages((int) Math.ceil((double) searchHits.getTotalHits() / request.getSize()))
                .pageIndex(request.getPage())
                .hasNext((request.getPage() * 1L) * request.getSize() < searchHits.getTotalHits())
                .hasPrevious(request.getPage() > 0)
                .build();

    }
}
