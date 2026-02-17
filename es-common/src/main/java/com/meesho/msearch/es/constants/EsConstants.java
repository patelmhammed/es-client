package com.meesho.msearch.es.constants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EsConstants {

    public static final String VERSION = "_version";
    public static final String VERSION_TYPE = "_version_type";
    public static final String ID = "_id";
    public static final String SCORE = "_score";
    public static final String CONFLICTS_RESOLUTION_STRATEGY = "conflicts_resolution_strategy";
    public static final String WAIT_FOR_COMPLETION = "wait_for_completion";
    public static final String SLICES = "slices";
    public static final String REQUESTS_PER_SECOND = "requests_per_second";
    public static final String DOC_AS_UPSERT = "doc_as_upsert";
    public static final Set<String> ES_SYSTEM_FIELDS = Set.of(VERSION, ID, SCORE, VERSION_TYPE);

    @Getter
    public enum VersionType {
        INTERNAL("internal"),
        EXTERNAL("external"),
        EXTERNAL_GTE("external_gte"),
        FORCE("force");

        private final String value;
        private static final Map<String, VersionType> versionTypeMap = new HashMap<>();

        static {
            for (VersionType versionType : VersionType.values()) {
                versionTypeMap.put(versionType.value, versionType);
            }
        }

        VersionType(String value) {
            this.value = value;
        }

        public static VersionType fromValue(String name) {
            VersionType versionType = versionTypeMap.get(name.toLowerCase());
            if (versionType == null) {
                throw new IllegalArgumentException("No enum constant " + VersionType.class.getCanonicalName() + "." + name);
            }
            return versionType;
        }
    }

    @Getter
    public enum ConflictsResolutionStrategy {
        ABORT("abort"),
        PROCEED("proceed");

        private final String value;
        private static final Map<String, ConflictsResolutionStrategy> conflictsResolutionStrategyMap = new HashMap<>();

        static {
            for (ConflictsResolutionStrategy strategy : ConflictsResolutionStrategy.values()) {
                conflictsResolutionStrategyMap.put(strategy.value, strategy);
            }
        }

        ConflictsResolutionStrategy(String value) {
            this.value = value;
        }

        public static ConflictsResolutionStrategy fromValue(String name) {
            ConflictsResolutionStrategy strategy = conflictsResolutionStrategyMap.get(name.toLowerCase());
            if (strategy == null) {
                throw new IllegalArgumentException("No enum constant " + ConflictsResolutionStrategy.class.getCanonicalName() + "." + name);
            }
            return strategy;
        }
    }
}
