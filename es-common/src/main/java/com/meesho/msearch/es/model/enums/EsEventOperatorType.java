package com.meesho.msearch.es.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EsEventOperatorType {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete");

    private static final Map<String, EsEventOperatorType> stringToEnumMap = new HashMap<>();
    private final String value;

    EsEventOperatorType(String value) {
        this.value = value;
    }

    static {
        for (EsEventOperatorType type : values()) {
            stringToEnumMap.put(type.value, type);
        }
    }

    public static EsEventOperatorType fromValue(String value) {
        return stringToEnumMap.get(value.toLowerCase());
    }
}
