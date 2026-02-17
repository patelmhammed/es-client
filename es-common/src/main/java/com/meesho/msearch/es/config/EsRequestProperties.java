package com.meesho.msearch.es.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsRequestProperties {
    private String host;
    private String clusterId;
    private String queryTimeout;
    private String indexName;
    private List<String> routingKeys;
}
