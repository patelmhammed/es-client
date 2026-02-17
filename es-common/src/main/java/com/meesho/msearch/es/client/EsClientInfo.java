package com.meesho.msearch.es.client;

import com.meesho.msearch.es.EsVersion;
import com.meesho.msearch.es.config.EsRequestProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class EsClientInfo {
    private EsVersion esVersion;
    private EsClient esClient;
    private EsRequestProperties requestProperties;
}
