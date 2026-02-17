package com.meesho.msearch.es.v813.client;

import javax.annotation.Nullable;

import com.meesho.msearch.es.client.EsClient;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportOptions;

public class EsV813Client extends ElasticsearchAsyncClient implements EsClient {
    public EsV813Client(ElasticsearchTransport transport) {
        super(transport);
    }

    public EsV813Client(ElasticsearchTransport transport, @Nullable TransportOptions transportOptions) {
        super(transport, transportOptions);
    }
}
