package com.meesho.msearch.es.client;

import com.meesho.msearch.es.EsVersion;
import com.meesho.msearch.es.config.EsConnectionProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages ES client factories registered by version-specific modules.
 * Version modules register their factories via registerClientFactory().
 */
@Slf4j
@Component
public class EsClientManager {

    private final Map<String, EsClientFactory> clientFactories = new ConcurrentHashMap<>();

    /**
     * Register a client factory for a specific ES version.
     * Called by host-controlled initialization during startup.
     */
    public void registerClientFactory(EsVersion version, EsClientFactory factory) {
        if (version == null || factory == null) {
            throw new IllegalArgumentException("Version and client factory must be non-null");
        }
        EsClientFactory existing = clientFactories.putIfAbsent(version.getValue(), factory);
        if (existing == null) {
            log.info("Registered ES client factory for version {}", version.getValue());
            return;
        }
        log.warn("Client factory for version {} already registered, skipping duplicate", version.getValue());
    }

    /**
     * Create a client for the given version using the registered factory.
     */
    public EsClient createClient(EsConnectionProperties properties, EsVersion version) {
        EsClientFactory factory = clientFactories.get(version.getValue());
        if (factory == null) {
            throw new IllegalStateException(
                    "No ES client factory registered for version " + version.getValue()
                            + ". Available versions: " + clientFactories.keySet());
        }
        return factory.createClient(properties);
    }

    /**
     * Check if a factory is registered for the given version.
     */
    public boolean hasFactory(EsVersion version) {
        return clientFactories.containsKey(version.getValue());
    }
}
