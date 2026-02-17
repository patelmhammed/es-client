package com.meesho.msearch.es.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsRetryConfig {
    @Builder.Default
    private int maxAttempts = 3;
    @Builder.Default
    private long durationInSeconds = 3;
    @Builder.Default
    private double jitterFactor = 0.5;
}
