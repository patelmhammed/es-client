package com.meesho.msearch.es.repository;

import com.meesho.msearch.es.EsVersion;
import com.meesho.msearch.es.client.EsClient;
import com.meesho.msearch.es.client.EsClientInfo;
import com.meesho.msearch.es.config.EsConnectionProperties;
import com.meesho.msearch.es.model.requests.EsSearchRequest;
import com.meesho.msearch.es.model.requests.EsWriteRequest;
import com.meesho.msearch.es.model.responses.EsSearchResponse;
import com.meesho.msearch.es.model.responses.EsWriteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class EsRepositoryFactoryTest {

    private EsRepositoryFactory factory;

    @BeforeEach
    void setUp() {
        EsRepository v8Repo = new TestRepository();
        EsRepository v7Repo = new TestRepository();
        factory = new EsRepositoryFactory();
        factory.registerRepository(EsVersion.V8_5, v8Repo);
        factory.registerRepository(EsVersion.V7, v7Repo);
    }

    @Test
    void getRepository_v8() {
        EsRepository repo = factory.getRepository(EsVersion.V8_5);
        assertNotNull(repo);
    }

    @Test
    void getRepository_v7() {
        EsRepository repo = factory.getRepository(EsVersion.V7);
        assertNotNull(repo);
    }

    @Test
    void getRepository_default() {
        EsRepository repo = factory.getRepository();
        assertNotNull(repo);
    }

    @Test
    void getRepository_unknown_throws() {
        assertThrows(IllegalArgumentException.class, () -> {
            EsRepositoryFactory emptyFactory = new EsRepositoryFactory();
            emptyFactory.getRepository(EsVersion.V8_5);
        });
    }

    private static class TestRepository implements EsRepository {
        @Override public EsClient createClient(EsConnectionProperties connectionProperties) { return new EsClient() { }; }
        @Override public CompletableFuture<EsSearchResponse> getDocuments(EsSearchRequest r, EsClientInfo c) { return CompletableFuture.completedFuture(null); }
        @Override public CompletableFuture<EsWriteResponse> indexBulkDocuments(EsWriteRequest r, EsClientInfo c) { return CompletableFuture.completedFuture(null); }
    }
}
