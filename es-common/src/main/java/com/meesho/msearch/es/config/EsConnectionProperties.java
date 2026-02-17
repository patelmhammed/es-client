package com.meesho.msearch.es.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Minimal ES connection config: only hosts, username, password, socket-timeout.
 * All other client settings use Elasticsearch client defaults.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsConnectionProperties {
    private List<String> hosts;
    private String username;
    private String password;
    private Integer socketTimeout;

    @Override
    public String toString() {
        return "EsConnectionProperties{" +
                "hosts=" + hosts +
                ", username='" + (username != null ? "***" : null) + '\'' +
                ", socketTimeout=" + socketTimeout +
                '}';
    }
}
