package com.meesho.msearch.es;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public enum EsVersion {
    V7("7"),
    V8_5("8.5"),
    V8_13("8.13"),
    V9_1("9.1");

    private final String value;

    EsVersion(String value) {
        this.value = value;
    }

    private static final Map<String, EsVersion> stringToEnumMap = new HashMap<>();

    static {
        for (EsVersion version : values()) {
            stringToEnumMap.put(version.value, version);
        }
    }

    public static EsVersion fromValue(String value) {
        EsVersion version = stringToEnumMap.get(value);
        if (version == null) {
            throw new IllegalArgumentException("Unknown Elasticsearch version: " + value);
        }
        return version;
    }
}
