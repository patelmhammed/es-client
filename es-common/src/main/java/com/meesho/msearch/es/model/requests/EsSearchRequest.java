package com.meesho.msearch.es.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsSearchRequest {
    private List<String> searchFields;
    private Integer limit;
}
