package com.meesho.msearch.es.repository;

import com.meesho.msearch.es.EsVersion;

/**
 * Marker interface for version-specific ES repository implementations.
 * Extends EsRepository with version identification.
 */
public interface EsVersionedRepository extends EsRepository {
    EsVersion getVersion();
}
