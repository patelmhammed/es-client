package com.meesho.msearch.es.v8.config;

import com.meesho.msearch.es.EsVersion;
import com.meesho.msearch.es.client.EsClientManager;
import com.meesho.msearch.es.config.EsRetryConfig;
import com.meesho.msearch.es.repository.EsRepositoryFactory;
import com.meesho.msearch.es.v8.repository.EsV8Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * Host-invoked registration helper for ES 8.5.
 * Registers both client factory and repository implementation.
 */
@Slf4j
public class EsV8ClientFactoryRegistrar {
    private EsV8ClientFactoryRegistrar() {
    }

    public static void register(EsClientManager esClientManager, EsRepositoryFactory repositoryFactory,
            EsRetryConfig retryConfig) {
        esClientManager.registerClientFactory(
                EsVersion.V8_5,
                EsV8Configuration::createClient
        );
        repositoryFactory.registerRepository(EsVersion.V8_5, new EsV8Repository(retryConfig));
        log.info("Registered ES 8.5 client factory and repository");
    }
}
