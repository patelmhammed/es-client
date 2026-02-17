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
import java.util.Optional;

public class EsV813Configuration {

    private static final Integer DEFAULT_MAX_CONNECTION_TOTAL = 30;
    private static final Integer DEFAULT_MAX_CONNECTION_PER_ROUTE = 10;

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

        for (String host : hosts) {
            String[] parts = host.split(":");
            String hostname = parts[0];
            int port = Integer.parseInt(parts[1]);
            httpHosts.add(new HttpHost(hostname, port, configs.getScheme()));
        }

        return RestClient.builder(httpHosts.toArray(new HttpHost[0]))
                .setRequestConfigCallback(
                        builder ->
                                builder
                                        .setConnectTimeout(configs.getConnectTimeout())
                                        .setSocketTimeout(configs.getSocketTimeout())
                                        .setConnectionRequestTimeout(configs.getConnectionRequestTimeout()))
                .setHttpClientConfigCallback(
                        httpAsyncClientBuilder -> {
                            httpAsyncClientBuilder
                                    .setMaxConnTotal(
                                            Optional.ofNullable(configs.getMaxConnectionTotal())
                                                    .filter(maxConnTotal -> maxConnTotal > 0)
                                                    .orElse(DEFAULT_MAX_CONNECTION_TOTAL))
                                    .setMaxConnPerRoute(
                                            Optional.ofNullable(configs.getMaxConnectionPerRoute())
                                                    .filter(maxConnPerRoute -> maxConnPerRoute > 0)
                                                    .orElse(DEFAULT_MAX_CONNECTION_PER_ROUTE));
                            setAuthenticatedRestClientBuilder(configs, httpAsyncClientBuilder);
                            return httpAsyncClientBuilder;
                        }
                );
    }

    private static void setAuthenticatedRestClientBuilder(EsConnectionProperties configs,
            HttpAsyncClientBuilder httpAsyncClientBuilder) {
        if (configs.getAuthEnabled()) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(configs.getUsername(), configs.getPassword()));
            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }
    }
}
