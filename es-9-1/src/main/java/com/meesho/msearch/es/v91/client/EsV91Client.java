package com.meesho.msearch.es.v91.client;

import javax.annotation.Nullable;

import com.meesho.msearch.es.client.EsClient;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportOptions;

public class EsV91Client extends ElasticsearchAsyncClient implements EsClient {
    public EsV91Client(ElasticsearchTransport transport) {
        super(transport);
    }

    public EsV91Client(ElasticsearchTransport transport, @Nullable TransportOptions transportOptions) {
        super(transport, transportOptions);
    }
}
