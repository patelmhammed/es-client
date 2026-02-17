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
public class EsConnectionProperties {
    private List<String> hosts;
    private Integer connectionRequestTimeout;
    private Integer connectTimeout;
    private Boolean authEnabled;
    private String username;
    private String password;
    private Integer maxConnectionTotal;
    private Integer maxConnectionPerRoute;
    private String scheme;
    private Integer socketTimeout;

    @Override
    public String toString() {
        return "EsConnectionProperties{" +
                "hosts=" + hosts +
                ", connectionRequestTimeout=" + connectionRequestTimeout +
                ", connectTimeout=" + connectTimeout +
                ", authEnabled=" + authEnabled +
                ", username='" + username + '\'' +
                ", maxConnectionTotal=" + maxConnectionTotal +
                ", maxConnectionPerRoute=" + maxConnectionPerRoute +
                ", scheme='" + scheme + '\'' +
                ", socketTimeout=" + socketTimeout +
                '}';
    }
}
