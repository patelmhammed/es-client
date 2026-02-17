package com.meesho.msearch.es.model.responses;

import com.meesho.msearch.es.model.EsRepositoryEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EsSearchResponse {
    private List<EsRepositoryEntity> documents;
    private long resultCount;
}
