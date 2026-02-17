package com.meesho.msearch.es.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsWriteResponse {
    private List<DocumentResponse> documentResponses;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DocumentResponse {
        private String id;
        private int statusCode;
        private String result;
        private String errorType;
        private String errorReason;
    }
}
