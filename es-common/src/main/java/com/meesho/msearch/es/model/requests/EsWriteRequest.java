package com.meesho.msearch.es.model.requests;

import com.fasterxml.jackson.databind.JsonNode;
import com.meesho.msearch.es.model.enums.EsEventOperatorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsWriteRequest {
    private List<EsIndexRecord> documents;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EsIndexRecord {
        private JsonNode record;
        private EsEventOperatorType operation;
    }
}
