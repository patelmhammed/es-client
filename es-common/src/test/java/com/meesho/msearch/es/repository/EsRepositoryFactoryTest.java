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
        EsVersionedRepository v8Repo = new TestVersionedRepository(EsVersion.V8_5);
        EsVersionedRepository v7Repo = new TestVersionedRepository(EsVersion.V7);
        factory = new EsRepositoryFactory();
        factory.registerRepository(v8Repo.getVersion(), v8Repo);
        factory.registerRepository(v7Repo.getVersion(), v7Repo);
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

    private static class TestVersionedRepository implements EsVersionedRepository {
        private final EsVersion version;

        TestVersionedRepository(EsVersion version) {
            this.version = version;
        }

        @Override public EsVersion getVersion() { return version; }
        @Override public EsClient createClient(EsConnectionProperties connectionProperties) { return new EsClient() { }; }
        @Override public CompletableFuture<EsSearchResponse> getDocuments(EsSearchRequest r, EsClientInfo c) { return CompletableFuture.completedFuture(null); }
        @Override public CompletableFuture<EsWriteResponse> indexBulkDocuments(EsWriteRequest r, EsClientInfo c) { return CompletableFuture.completedFuture(null); }
    }
}
