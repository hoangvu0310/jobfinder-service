package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.SearchResultPaginated;
import com.meilisearch.sdk.model.Settings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
public class MeiliSearchService {
  private final Client meilisearchClient;

  private ObjectMapper objectMapper;

  // Cache index theo tên để tránh gọi getIndex nhiều lần
  private final Map<String, Index> indexCache = new ConcurrentHashMap<>();

  public void initializeIndex(
      String indexName,
      String[] searchableAttrs,
      String[] filterableAttrs,
      String[] sortableAttrs,
      Settings otherSettings
  ) throws MeilisearchException {

    Index index = getOrCreateIndex(indexName);

    Settings settings = new Settings()
        .setSearchableAttributes(searchableAttrs)
        .setFilterableAttributes(filterableAttrs)
        .setSortableAttributes(sortableAttrs);

    if (otherSettings != null) {
      otherSettings.setSearchableAttributes(settings.getSearchableAttributes());
      otherSettings.setFilterableAttributes(settings.getFilterableAttributes());
      otherSettings.setSortableAttributes(settings.getSortableAttributes());

      // Merge settings
      index.updateSettings(otherSettings);
      return;
    }

    index.updateSettings(settings);
  }

  public <T> T getDocument(String indexName, Long id, Class<T> documentClass) {
    Index index = getOrCreateIndex(indexName);
    return index.getDocument(id.toString(), documentClass);
  }

  public <T> void indexDocument(String indexName, T document) throws MeilisearchException {
    Index index = getOrCreateIndex(indexName);
    index.addDocuments(objectMapper.writeValueAsString(document));
  }

  public <T> void indexDocuments(String indexName, List<T> documents) throws MeilisearchException {
    if (documents == null || documents.isEmpty()) return;
    Index index = getOrCreateIndex(indexName);
    index.addDocuments(objectMapper.writeValueAsString(documents));
  }

  public <T> void updateDocuments(String indexName, List<T> documents) throws MeilisearchException {
    if (documents == null || documents.isEmpty()) return;
    Index index = getOrCreateIndex(indexName);
    index.updateDocuments(objectMapper.writeValueAsString(documents));
  }

  public void deleteDocument(String indexName, String documentId) throws MeilisearchException {
    Index index = getOrCreateIndex(indexName);
    index.deleteDocument(documentId);
  }

  public <T> PageableResponse<T> search(
      String indexName,
      String keyword,
      @NonNull PagingDTO pagingDTO,
      String[] filterString,
      String[] sortString,
      Function<HashMap<String, Object>, T> mapper
  ) {
    Index index = getOrCreateIndex(indexName);

    SearchRequest searchRequest = SearchRequest.builder()
        .q(keyword)
        .hitsPerPage(pagingDTO.getPageSize())
        .page(pagingDTO.getPageNumber())
        .filter(filterString)
        .sort(sortString)
        .build();

    SearchResultPaginated result = (SearchResultPaginated) index.search(searchRequest);
    List<T> listData = result.getHits().stream().map(mapper).toList();
    return new PageableResponse<>(
        listData,
        result.getPage(),
        result.getHitsPerPage(),
        result.getTotalHits(),
        result.getTotalPages()
    );
  }

  // Helper: lấy hoặc cache index
  private Index getOrCreateIndex(String indexName) {
    return indexCache.computeIfAbsent(indexName, name -> {
      try {
        return meilisearchClient.getIndex(name);
      } catch (MeilisearchException e) {
        try {
          meilisearchClient.createIndex(name);
          return meilisearchClient.getIndex(name);
        } catch (MeilisearchException ex) {
          log.error("Cannot create/get Meilisearch index: {}", name, ex);
          throw new RuntimeException("Meilisearch index error: " + name, ex);
        }
      }
    });
  }
}
