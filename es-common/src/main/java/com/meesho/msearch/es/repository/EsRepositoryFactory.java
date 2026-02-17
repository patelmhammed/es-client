package com.meesho.msearch.es.repository;

import com.meesho.msearch.es.EsVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory that routes to the correct version-specific EsRepository.
 * Repositories are registered explicitly by host startup initialization.
 */
@Slf4j
@Component
public class EsRepositoryFactory {

    private final Map<EsVersion, EsRepository> repositoryMap = new ConcurrentHashMap<>();

    public void registerRepository(EsVersion version, EsRepository repository) {
        if (version == null || repository == null) {
            throw new IllegalArgumentException("Version and repository must be non-null");
        }
        EsRepository existing = repositoryMap.putIfAbsent(version, repository);
        if (existing == null) {
            log.info("Registered ES repository for version {}", version);
            return;
        }
        log.warn("Repository for version {} already registered, skipping duplicate", version);
    }

    /**
     * Get the repository for the specified ES version.
     */
    public EsRepository getRepository(EsVersion version) {
        EsRepository repository = repositoryMap.get(version);
        if (repository == null) {
            throw new IllegalArgumentException(
                    "No ES repository registered for version " + version
                            + ". Available: " + repositoryMap.keySet());
        }
        return repository;
    }

    /**
     * Get the repository, defaulting to V8 if version is null.
     */
    public EsRepository getRepository() {
        return getRepository(EsVersion.V8_5);
    }
}
