package com.meesho.msearch.es.v8.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.util.ObjectBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meesho.msearch.es.client.EsClient;
import com.meesho.msearch.es.client.EsClientInfo;
import com.meesho.msearch.es.config.EsConnectionProperties;
import com.meesho.msearch.es.config.EsRequestProperties;
import com.meesho.msearch.es.constants.EsConstants;
import com.meesho.msearch.es.model.EsRepositoryEntity;
import com.meesho.msearch.es.model.requests.EsSearchRequest;
import com.meesho.msearch.es.model.requests.EsWriteRequest;
import com.meesho.msearch.es.model.responses.EsSearchResponse;
import com.meesho.msearch.es.model.responses.EsWriteResponse;
import com.meesho.msearch.es.repository.EsRepository;
import com.meesho.msearch.es.util.EsRetryUtils;
import com.meesho.msearch.es.v8.config.EsV8Configuration;
import com.meesho.msearch.es.v8.client.EsV8Client;
import com.meesho.msearch.es.v8.repository.utils.EsV8WriteUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class EsV8Repository implements EsRepository {
    private final com.meesho.msearch.es.config.EsRetryConfig retryConfig;

    public EsV8Repository(com.meesho.msearch.es.config.EsRetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }

    @Override
    public EsClient createClient(EsConnectionProperties connectionProperties) {
        return EsV8Configuration.createClient(connectionProperties);
    }

    @Override
    public CompletableFuture<EsSearchResponse> getDocuments(EsSearchRequest request, EsClientInfo clientInfo) {
        log.info("Search request routed to ES client version 8.5");
        EsRequestProperties requestProperties = clientInfo.getRequestProperties();
        EsV8Client client = (EsV8Client) clientInfo.getEsClient();
        int size = request.getLimit() == null ? 10 : request.getLimit();
        List<String> fields = request.getSearchFields() == null ? Collections.emptyList() : request.getSearchFields();

        return client.search(s -> getSearchRequest(s, requestProperties.getIndexName(), size, fields),
                JsonNode.class)
                .thenApply(response -> {
                    List<EsRepositoryEntity> documents = response.hits().hits().stream().map(hit -> {
                        ObjectNode sourceNode = hit.source() instanceof ObjectNode ? (ObjectNode) hit.source() : null;
                        if (sourceNode != null && hit.version() != null) {
                            sourceNode.put(EsConstants.VERSION, hit.version());
                        }
                        return EsRepositoryEntity.builder()
                                .entityId(Objects.requireNonNullElse(hit.id(), ""))
                                .metadata(sourceNode == null ? null : sourceNode.toString())
                                .build();
                    }).toList();
                    long resultCount = response.hits().total() == null ? documents.size() : response.hits().total().value();
                    return EsSearchResponse.builder().documents(documents).resultCount(resultCount).build();
                });
    }

    private static SearchRequest.Builder getSearchRequest(SearchRequest.Builder s, String indexName, Integer size, List<String> fields) {
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        boolBuilder.must(m -> m.matchAll(ma -> ma));

        // Build Query object
        Query query = new Query.Builder()
                .bool(boolBuilder.build())
                .build();

        SearchRequest.Builder builder = s.index(indexName)
                .size(size)
                .source(getSource(fields))
                .query(query);
        return builder;
    }

    private static java.util.function.Function<SourceConfig.Builder, ObjectBuilder<SourceConfig>> getSource(
            List<String> responseFields) {

        return x -> x.filter(y -> y.includes(responseFields));
    }

    @Override
    public CompletableFuture<EsWriteResponse> indexBulkDocuments(EsWriteRequest request, EsClientInfo clientInfo) {
        log.info("Write request routed to ES client version 8.5");
        EsRequestProperties requestProperties = clientInfo.getRequestProperties();
        EsV8Client client = (EsV8Client) clientInfo.getEsClient();

        var documents = request.getDocuments() == null ? Collections.<EsWriteRequest.EsIndexRecord>emptyList() : request.getDocuments();
        var bulkRequest = EsV8WriteUtil.getBulkRequestBuilder(documents, requestProperties).build();

        return EsRetryUtils.retryFuture(
                        () -> client.bulk(bulkRequest).thenApply(response -> EsWriteResponse.builder()
                                .documentResponses(response.items().stream()
                                        .map(item -> EsWriteResponse.DocumentResponse.builder()
                                                .id(item.id())
                                                .statusCode(item.status())
                                                .result(item.result())
                                                .errorReason(item.error() != null ? item.error().reason() : null)
                                                .errorType(item.error() != null ? item.error().type() : null)
                                                .build())
                                        .toList())
                                .build()),
                        retryConfig.getMaxAttempts(),
                        Duration.ofSeconds(retryConfig.getDurationInSeconds()),
                        retryConfig.getJitterFactor(),
                        "EsV8Repository.indexBulkDocuments")
                .whenComplete((ignored, ex) -> {
                    if (ex != null) {
                        log.error("Bulk index failed", ex);
                    }
                });
    }
}
