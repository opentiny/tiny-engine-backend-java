package com.tinyengine.it.rag.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;

import java.time.Duration;

/** Creates Chroma embedding stores from validated connection settings. */
public final class ChromaStoreFactory {
    private ChromaStoreFactory() {
    }

    public static EmbeddingStore<TextSegment> create(
            final String baseUrl, final String collectionName, final Duration timeout) {
        final ChromaEmbeddingStore.Builder builder = ChromaEmbeddingStore.builder();
        builder.baseUrl(baseUrl);
        builder.collectionName(collectionName);
        builder.timeout(timeout);
        return builder.build();
    }
}
