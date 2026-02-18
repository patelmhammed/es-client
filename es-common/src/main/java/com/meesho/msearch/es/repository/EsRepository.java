package com.meesho.msearch.es.repository;

import com.meesho.msearch.es.client.EsClient;
import com.meesho.msearch.es.client.EsClientInfo;
import com.meesho.msearch.es.config.EsConnectionProperties;
import com.meesho.msearch.es.model.requests.EsSearchRequest;
import com.meesho.msearch.es.model.requests.EsWriteRequest;
import com.meesho.msearch.es.model.responses.EsSearchResponse;
import com.meesho.msearch.es.model.responses.EsWriteResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Core repository interface for Elasticsearch data operations.
 * Version-specific modules implement this interface.
 */
public interface EsRepository {

    EsClient createClient(EsConnectionProperties connectionProperties);

    CompletableFuture<EsSearchResponse> getDocuments(EsSearchRequest request, EsClientInfo clientInfo);

    CompletableFuture<EsWriteResponse> indexBulkDocuments(EsWriteRequest request, EsClientInfo clientInfo);
}
