package com.meesho.msearch.es.v91.repository.utils;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.meesho.msearch.es.config.EsRequestProperties;
import com.meesho.msearch.es.model.requests.EsWriteRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class EsV91WriteUtil {

    public static BulkRequest.Builder getBulkRequestBuilder(
            List<EsWriteRequest.EsIndexRecord> documents,
            EsRequestProperties esRequestProperties) {
        BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();

        documents.forEach(document -> {
            String id = getDocId(document.getRecord());

            switch (document.getOperation()) {
                case INSERT -> bulkRequestBuilder.operations(op -> op
                        .index(idx -> {
                            var builder = idx
                                    .id(id)
                                    .index(esRequestProperties.getIndexName())
                                    .document(document.getRecord());
                            return builder;
                        }));
                case UPDATE -> bulkRequestBuilder.operations(op -> op
                        .update(up -> {
                            var builder = up
                                    .id(id)
                                    .index(esRequestProperties.getIndexName())
                                    .action(ua -> ua
                                            .doc(document.getRecord()));
                            return builder;
                        }));
                case DELETE -> bulkRequestBuilder.operations(op -> op
                        .delete(del -> {
                            var builder = del
                                    .id(id)
                                    .index(esRequestProperties.getIndexName());
                            return builder;
                        }));
            }
        });
        log.info("Write bulk request for {} documents", documents.size());
        return bulkRequestBuilder;
    }

    private static String getDocId(JsonNode record) {
        Object recordId = record.get("id");
        if (recordId instanceof TextNode) {
            return ((TextNode) recordId).asText();
        } else {
            return recordId != null ? recordId.toString() : null;
        }
    }
}
