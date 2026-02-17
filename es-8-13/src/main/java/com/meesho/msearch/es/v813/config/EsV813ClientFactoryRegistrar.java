package com.meesho.msearch.es.v813.config;

import com.meesho.msearch.es.EsVersion;
import com.meesho.msearch.es.client.EsClientManager;
import com.meesho.msearch.es.config.EsRetryConfig;
import com.meesho.msearch.es.repository.EsRepositoryFactory;
import com.meesho.msearch.es.v813.repository.EsV813Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * Host-invoked registration helper for ES 8.13.
 * Registers both client factory and repository implementation.
 */
@Slf4j
public class EsV813ClientFactoryRegistrar {
    private EsV813ClientFactoryRegistrar() {
    }

    public static void register(EsClientManager esClientManager, EsRepositoryFactory repositoryFactory,
            EsRetryConfig retryConfig) {
        esClientManager.registerClientFactory(
                EsVersion.V8_13,
                EsV813Configuration::createClient
        );
        repositoryFactory.registerRepository(EsVersion.V8_13, new EsV813Repository(retryConfig));
        log.info("Registered ES 8.13 client factory and repository");
    }
}
