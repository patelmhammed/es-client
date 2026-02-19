package com.meesho.msearch.es.client;

import java.io.IOException;

/**
 * Marker interface for Elasticsearch client instances.
 * Implemented by version-specific ES clients (V7, V8, etc.).
 */
public interface EsClient {
    void close() throws IOException;
}
