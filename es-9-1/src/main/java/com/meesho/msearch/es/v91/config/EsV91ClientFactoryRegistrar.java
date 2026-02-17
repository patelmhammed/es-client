package com.meesho.msearch.es.v91.config;

import com.meesho.msearch.es.EsVersion;
import com.meesho.msearch.es.client.EsClientManager;
import com.meesho.msearch.es.config.EsRetryConfig;
import com.meesho.msearch.es.repository.EsRepositoryFactory;
import com.meesho.msearch.es.v91.repository.EsV91Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * Host-invoked registration helper for ES 9.1.
 * Registers both client factory and repository implementation.
 */
@Slf4j
public class EsV91ClientFactoryRegistrar {
    private EsV91ClientFactoryRegistrar() {
    }

    public static void register(EsClientManager esClientManager, EsRepositoryFactory repositoryFactory,
            EsRetryConfig retryConfig) {
        esClientManager.registerClientFactory(
                EsVersion.V9_1,
                connectionProperties -> EsV91Configuration.createClient(connectionProperties)
        );
        repositoryFactory.registerRepository(EsVersion.V9_1, new EsV91Repository(retryConfig));
        log.info("Registered ES 9.1 client factory and repository");
    }
}
