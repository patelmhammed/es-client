package com.meesho.msearch.es.v813.config;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.meesho.msearch.es.config.EsConnectionProperties;
import com.meesho.msearch.es.v813.client.EsV813Client;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * ES 8.13 client config: only hosts, username, password, socket-timeout; rest use client defaults.
 */
public class EsV813Configuration {

    private static final String DEFAULT_SCHEME = "http";

    public static EsV813Client createClient(EsConnectionProperties configs) {
        RestClientBuilder restClientBuilder = getRestClientBuilder(configs);
        ElasticsearchTransport transport = new RestClientTransport(
                restClientBuilder.build(),
                new JacksonJsonpMapper()
        );
        return new EsV813Client(transport);
    }

    public static RestClientBuilder getRestClientBuilder(EsConnectionProperties configs) {
        List<String> hosts = configs.getHosts();
        List<HttpHost> httpHosts = new ArrayList<>();
        String scheme = DEFAULT_SCHEME;
        for (String host : hosts) {
            String[] parts = host.split(":");
            String hostname = parts[0];
            int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 9200;
            httpHosts.add(new HttpHost(hostname, port, scheme));
        }

        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));
        if (configs.getSocketTimeout() != null && configs.getSocketTimeout() > 0) {
            builder.setRequestConfigCallback(
                    requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(configs.getSocketTimeout()));
        }
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            setAuthenticatedRestClientBuilder(configs, httpAsyncClientBuilder);
            return httpAsyncClientBuilder;
        });
        return builder;
    }

    private static void setAuthenticatedRestClientBuilder(EsConnectionProperties configs,
            HttpAsyncClientBuilder httpAsyncClientBuilder) {
        if (configs.getUsername() != null && !configs.getUsername().isBlank()) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(configs.getUsername(),
                            configs.getPassword() != null ? configs.getPassword() : ""));
            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }
    }
}
