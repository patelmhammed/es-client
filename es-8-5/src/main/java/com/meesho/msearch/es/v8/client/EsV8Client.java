package com.meesho.msearch.es.v8.client;

import javax.annotation.Nullable;

import com.meesho.msearch.es.client.EsClient;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportOptions;

import java.io.IOException;

public class EsV8Client extends ElasticsearchAsyncClient implements EsClient {
    public EsV8Client(ElasticsearchTransport transport) {
        super(transport);
    }

    public EsV8Client(ElasticsearchTransport transport, @Nullable TransportOptions transportOptions) {
        super(transport, transportOptions);
    }

    @Override
    public void close() throws IOException {

    }
}
