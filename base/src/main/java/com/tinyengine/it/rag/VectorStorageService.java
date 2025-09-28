package com.tinyengine.it.rag;

import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.rag.entity.RAGConfig;
import com.tinyengine.it.rag.entity.VectorDocument;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class VectorStorageService {
    private static EmbeddingModel embeddingModel = null;
    private static EmbeddingStore<TextSegment> embeddingStore = null;
    private final ExecutorService executorService;

    /**
     * 使用 ChromaEmbeddingStore 的构造函数 - 修正版本
     */
    public VectorStorageService(EmbeddingModel embeddingModel,  EmbeddingStore<TextSegment> embeddingStore) {
        VectorStorageService.embeddingModel = embeddingModel;
        VectorStorageService.embeddingStore = embeddingStore;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * 添加文档到知识库
     */
    public static VectorDocument initializeKnowledgeBase(List<String> documentPaths) {
        return initializeKnowledgeBase(documentPaths, null);
    }

    /**
     * 添加文档到知识库（支持自定义元数据）
     */
    public static VectorDocument initializeKnowledgeBase(List<String> documentPaths, String documentSetId) {
        try {
            List<Document> documents = loadDocuments(documentPaths, documentSetId);

            if (documents.isEmpty()) {
                throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "未成功加载任何文档");
            }

            log.info("成功加载 {} 个文档", documents.size());

            // 文档切分
            List<TextSegment> segments = splitDocuments(documents);
            log.info("生成 {} 个文本段", segments.size());

            // 向量化并存储
            return embedAndStore(segments);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("文档添加到知识库失败", e);
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "文档处理失败: " + e.getMessage());
        }
    }

    /**
     * 加载文档（支持元数据）
     */
    private static List<Document> loadDocuments(List<String> documentPaths, String documentSetId) {
        List<Document> documents = new ArrayList<>();

        for (String path : documentPaths) {
            try {
                Path filePath = Paths.get(path);
                Document document;

                if (path.toLowerCase().endsWith(".pdf")) {
                    document = FileSystemDocumentLoader.loadDocument(filePath);
                } else if (path.toLowerCase().endsWith(".txt") || path.toLowerCase().endsWith(".md")) {
                    document = FileSystemDocumentLoader.loadDocument(filePath, new TextDocumentParser());
                } else {
                    log.warn("不支持的文档格式: {}", path);
                    continue;
                }

                // 添加元数据
                if (documentSetId != null) {
                    document.metadata().put("documentSetId", documentSetId);
                }
                document.metadata().put("source", path);
                document.metadata().put("timestamp", String.valueOf(System.currentTimeMillis()));

                documents.add(document);
                log.info("✓ 加载文档: {}", path);

            } catch (Exception e) {
                log.error("✗ 加载文档失败: {} - {}", path, e.getMessage());
            }
        }

        return documents;
    }

    /**
     * 文档切分
     */
    private static List<TextSegment> splitDocuments(List<Document> documents) {
        DocumentSplitter splitter = DocumentSplitters.recursive(
                RAGConfig.CHUNK_SIZE,
                RAGConfig.CHUNK_OVERLAP
        );
        return splitter.splitAll(documents);
    }

    /**
     * 向量化并存储（优化性能版本）
     */
    private static VectorDocument embedAndStore(List<TextSegment> segments) {
        log.info("开始向量化存储...");
        long startTime = System.currentTimeMillis();

        int successCount = 0;
        int errorCount = 0;

        // 批量处理，提高性能
        int batchSize = 50;
        for (int i = 0; i < segments.size(); i += batchSize) {
            int end = Math.min(i + batchSize, segments.size());
            List<TextSegment> batch = segments.subList(i, end);

            BatchResult result = processBatch(batch, i, segments.size());
            successCount += result.successCount;
            errorCount += result.errorCount;
        }

        long endTime = System.currentTimeMillis();
        log.info("向量化完成: {} 成功, {} 失败, 耗时: {}ms", successCount, errorCount, (endTime - startTime));

        return new VectorDocument(successCount, errorCount);
    }

    /**
     * 处理批次数据的内部类
     */
    private static class BatchResult {
        int successCount;
        int errorCount;

        BatchResult(int successCount, int errorCount) {
            this.successCount = successCount;
            this.errorCount = errorCount;
        }
    }

    /**
     * 处理批次数据
     */
    private static BatchResult processBatch(List<TextSegment> batch, int startIndex, int totalSize) {
        int successCount = 0;
        int errorCount = 0;

        List<Embedding> embeddings = new ArrayList<>();
        List<TextSegment> segmentsToStore = new ArrayList<>();

        for (int i = 0; i < batch.size(); i++) {
            TextSegment segment = batch.get(i);
            try {
                Embedding embedding = embeddingModel.embed(segment.text()).content();
                embeddings.add(embedding);
                segmentsToStore.add(segment);
                successCount++;

                if ((startIndex + i + 1) % 100 == 0) {
                    log.info("已处理 {}/{} 个文本段", (startIndex + i + 1), totalSize);
                }
            } catch (Exception e) {
                errorCount++;
                log.error("向量化失败 [{}]: {}", (startIndex + i + 1),
                        segment.text().substring(0, Math.min(100, segment.text().length())));
            }
        }

        // 批量存储到 Chroma
        if (!embeddings.isEmpty()) {
            try {
                // 修正：使用正确的批量添加方法
                for (int i = 0; i < embeddings.size(); i++) {
                    embeddingStore.add(embeddings.get(i), segmentsToStore.get(i));
                }
                log.debug("成功存储 {} 个文本段到 Chroma", embeddings.size());
            } catch (Exception e) {
                log.error("批量存储到 Chroma 失败", e);
                errorCount += embeddings.size(); // 标记为失败
                successCount -= embeddings.size();
            }
        }

        return new BatchResult(successCount, errorCount);
    }

    /**
     * 向量库检索
     */
    public List<EmbeddingMatch<TextSegment>> search(String query, int maxResults, double minScore) {
        return search(query, maxResults, minScore, null);
    }

    /**
     * 带过滤条件的检索 - 修正版本
     */
    public List<EmbeddingMatch<TextSegment>> search(String query, int maxResults, double minScore, String documentSetId) {
        try {
            Embedding queryEmbedding = embeddingModel.embed(query).content();

            // 修正：使用正确的搜索请求构建方式
            EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .maxResults(maxResults)
                    .minScore(minScore)
                    .build();

            List<EmbeddingMatch<TextSegment>> results = embeddingStore.search(searchRequest).matches();

            // 如果指定了文档集ID，进行过滤
            if (documentSetId != null) {
                results = filterByDocumentSetId(results, documentSetId);
            }

            log.info("检索到 {} 个相关文档", results.size());
            return results;

        } catch (Exception e) {
            log.error("检索失败", e);
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "检索失败: " + e.getMessage());
        }
    }

    /**
     * 根据文档集ID过滤结果
     */
    private List<EmbeddingMatch<TextSegment>> filterByDocumentSetId(
            List<EmbeddingMatch<TextSegment>> results, String documentSetId) {

        List<EmbeddingMatch<TextSegment>> filteredResults = new ArrayList<>();

        for (EmbeddingMatch<TextSegment> match : results) {
            String docSetId = match.embedded().metadata().getString("documentSetId");
            if (documentSetId.equals(docSetId)) {
                filteredResults.add(match);
            }
        }

        return filteredResults;
    }

    /**
     * 完整的问答流程
     */
    public List<EmbeddingMatch<TextSegment>> askQuestion(String question) {
        return askQuestion(question, RAGConfig.MAX_RESULTS, RAGConfig.MIN_SCORE, null);
    }

    public List<EmbeddingMatch<TextSegment>> askQuestion(String question, int maxResults, double minScore, String documentSetId) {
        try {
            long startTime = System.currentTimeMillis();

            // 1. 检索相关文档
            List<EmbeddingMatch<TextSegment>> searchResults = search(question, maxResults, minScore, documentSetId);
            long retrievalTime = System.currentTimeMillis() - startTime;

            log.info("检索耗时: {}ms", retrievalTime);

            if (searchResults.isEmpty()) {
                log.warn("未找到相关文档");
                return searchResults;
            }

            // 打印检索结果
            for (int i = 0; i < Math.min(3, searchResults.size()); i++) {
                EmbeddingMatch<TextSegment> match = searchResults.get(i);
                log.debug("结果 {} - 相似度: {:.4f}", i + 1, match.score());
            }

            return searchResults;

        } catch (Exception e) {
            log.error("问答流程失败", e);
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "问答失败: " + e.getMessage());
        }
    }

    /**
     * 清空向量库
     */
    public void clearVectorStore() {
        try {

            // 在 0.29.0 版本中，可能需要通过其他方式清空
            log.info("请通过 Chroma API 清空向量库数据");
        } catch (Exception e) {
            log.error("清空向量库失败", e);
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "清空向量库失败");
        }
    }

    /**
     * 关闭资源
     */
    public void shutdown() {
        executorService.shutdown();
        log.info("VectorStorageService 已关闭");
    }

    /**
     * 获取向量库统计信息
     */
    public void getVectorStoreStats() {
        try {
            log.info("向量库服务运行中 - 模型: {}, 存储: {}",
                    embeddingModel.getClass().getSimpleName(),
                    embeddingStore.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("获取向量库统计信息失败", e);
        }
    }
}
