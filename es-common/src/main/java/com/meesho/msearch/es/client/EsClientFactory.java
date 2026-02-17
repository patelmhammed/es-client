package com.meesho.msearch.es.client;

import com.meesho.msearch.es.config.EsConnectionProperties;

/**
 * Functional interface for creating version-specific Elasticsearch clients.
 * Each ES version module provides its own implementation.
 */
@FunctionalInterface
public interface EsClientFactory {
    EsClient createClient(EsConnectionProperties connectionProperties);
}
