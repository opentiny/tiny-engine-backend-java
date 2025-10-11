/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.rag.service;

import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.rag.entity.BatchDeleteResult;
import com.tinyengine.it.rag.entity.BatchResult;
import com.tinyengine.it.rag.entity.DeleteResult;
import com.tinyengine.it.rag.entity.EmbeddingMatchDto;
import com.tinyengine.it.rag.config.RAGConfig;
import com.tinyengine.it.rag.entity.SearchRequest;
import com.tinyengine.it.rag.entity.VectorDocument;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 存储服务 - 支持动态集合管理
 */
@Slf4j
@Service
public class StorageService {
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    private final RAGConfig ragConfig = new RAGConfig();

    // 支持的集合列表
    private static final List<String> SUPPORTED_COLLECTIONS = List.of(
        "tinyengine_documents",
        "agent_documents"
    );

    // 默认集合
    private static final String DEFAULT_COLLECTION = "tinyengine_documents";

    // 集合映射配置
    private final Map<String, String> collectionMapping = new HashMap<>();

    /**
     * 支持的文档格式
     */
    private static final List<String> SUPPORTED_FORMATS = List.of(
        ".pdf", ".txt", ".md", ".sql", ".java", ".py", ".js", ".ts",
        ".html", ".css", ".xml", ".json", ".yaml", ".yml", ".properties",
        ".sh", ".bat", ".cmd", ".c", ".cpp", ".h", ".hpp"
    );

    /**
     * 文本文件格式（使用 TextDocumentParser）
     */
    private static final List<String> TEXT_FORMATS = List.of(
        ".txt", ".md", ".sql", ".java", ".py", ".js", ".ts",
        ".html", ".css", ".xml", ".json", ".yaml", ".yml", ".properties",
        ".sh", ".bat", ".cmd", ".c", ".cpp", ".h", ".hpp"
    );

    /**
     * Check if file format is supported
     */
    private boolean isSupportedFormat(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        return SUPPORTED_FORMATS.stream().anyMatch(format -> fileName.endsWith(format));
    }

    /**
     * 构造函数
     */
    public StorageService(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        log.info("StorageService initialized with support for {} file formats", SUPPORTED_FORMATS.size());

        // 初始化集合映射
        initializeCollectionMapping();
    }

    /**
     * 初始化集合映射配置
     */
    private void initializeCollectionMapping() {
        // 配置特定文件类型到集合的映射
        collectionMapping.put("agent", "agent_documents");
        collectionMapping.put("tinyengine", "tinyengine_documents");

        log.info("Collection mapping initialized: {}", collectionMapping);
    }

    /**
     * 检查集合名称是否有效
     */
    private boolean isValidCollection(String collectionName) {
        return SUPPORTED_COLLECTIONS.contains(collectionName);
    }

    /**
     * 自动扫描文件夹并添加文档到知识库
     */
    public VectorDocument autoAddFolderToKnowledgeBase() {
        try {
            String folderPath = System.getenv("FOLDER_PATH");
            if (folderPath == null || folderPath.isBlank()) {
                throw new ServiceException(ExceptionEnum.CM329.getResultCode(), "FOLDER_PATH does not exist: " + folderPath);
            }
            // 验证文件夹路径
            Path folder = Paths.get(folderPath);
            if (!Files.exists(folder) || !Files.isDirectory(folder)) {
                throw new ServiceException(ExceptionEnum.CM329.getResultCode(), "Folder does not exist: " + folderPath);
            }

            // 扫描文件夹中的所有支持的文件
            List<String> filePaths = scanSupportedFiles(folderPath);

            if (filePaths.isEmpty()) {
                throw new ServiceException(ExceptionEnum.CM329.getResultCode(),
                    "No supported file formats found in folder. Supported formats: " + String.join(", ",
                         SUPPORTED_FORMATS));
            }

            log.info("Found {} supported files in folder: {}", filePaths.size(), folderPath);

            return initializeKnowledgeBase(filePaths);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to auto add folder to knowledge base", e);
            throw new ServiceException(ExceptionEnum.CM330.getResultCode(), "Auto add folder failed: " + e.getMessage());
        }
    }

    /**
     * 扫描文件夹中所有支持的文件
     */
    private List<String> scanSupportedFiles(String folderPath) {
        List<String> supportedFiles = new ArrayList<>();

        try {
            Path folder = Paths.get(folderPath);
            // 使用 Files.walk 递归扫描子文件夹
            Files.walk(folder)
                .filter(Files::isRegularFile)
                .filter(this::isSupportedFormat)
                .forEach(filePath -> {
                    supportedFiles.add(filePath.toString());
                    log.debug("Found supported file: {}", filePath);
                });

            supportedFiles.sort(String::compareTo);

        } catch (IOException e) {
            log.error("Failed to scan folder: {}", folderPath, e);
            throw new ServiceException(ExceptionEnum.CM333.getResultCode(), ExceptionEnum.CM333.getResultMsg());
        }

        return supportedFiles;
    }


    /**
     * 根据文档路径和自定义集合确定目标集合
     */
    private String determineCollectionName(String filePath, String customCollection) {
        // 如果指定了自定义集合，优先使用
        if (customCollection != null && !customCollection.trim().isEmpty()) {
            if (!isValidCollection(customCollection)) {
                log.warn("Invalid collection specified: {}, using default: {}", customCollection, DEFAULT_COLLECTION);
                return DEFAULT_COLLECTION;
            }
            return customCollection;
        }

        // 根据文件路径自动判断集合
        if (filePath != null) {
            String lowerPath = filePath.toLowerCase();
            // 如果路径包含特定关键词，映射到对应集合
            for (Map.Entry<String, String> entry : collectionMapping.entrySet()) {
                if (lowerPath.contains(entry.getKey())) {
                    log.info("Auto-mapped file {} to collection: {}", filePath, entry.getValue());
                    return entry.getValue();
                }
            }
        }

        // 默认集合
        return DEFAULT_COLLECTION;
    }

    /**
     * 检查文件格式是否支持
     */
    private boolean isSupportedFormat(String filePath) {
        if (filePath == null) return false;

        String lowerPath = filePath.toLowerCase();
        return SUPPORTED_FORMATS.stream().anyMatch(lowerPath::endsWith);
    }

    /**
     * 检查是否为文本格式
     */
    private boolean isTextFormat(String filePath) {
        if (filePath == null) return false;

        String lowerPath = filePath.toLowerCase();
        return TEXT_FORMATS.stream().anyMatch(lowerPath::endsWith);
    }

    /**
     * 获取文件格式描述
     */
    private String getFileFormatDescription(String filePath) {
        if (filePath == null) return "unknown";

        String lowerPath = filePath.toLowerCase();
        if (lowerPath.endsWith(".pdf")) return "PDF Document";
        if (lowerPath.endsWith(".sql")) return "SQL Script";
        if (lowerPath.endsWith(".java")) return "Java Source";
        if (lowerPath.endsWith(".py")) return "Python Script";
        if (lowerPath.endsWith(".js")) return "JavaScript";
        if (lowerPath.endsWith(".ts")) return "TypeScript";
        if (lowerPath.endsWith(".html")) return "HTML Document";
        if (lowerPath.endsWith(".css")) return "CSS Stylesheet";
        if (lowerPath.endsWith(".xml")) return "XML Document";
        if (lowerPath.endsWith(".json")) return "JSON Data";
        if (lowerPath.endsWith(".yaml") || lowerPath.endsWith(".yml")) return "YAML Configuration";
        if (lowerPath.endsWith(".properties")) return "Properties File";
        if (lowerPath.endsWith(".sh")) return "Shell Script";
        if (lowerPath.endsWith(".bat") || lowerPath.endsWith(".cmd")) return "Batch File";
        if (lowerPath.endsWith(".c")) return "C Source";
        if (lowerPath.endsWith(".cpp") || lowerPath.endsWith(".h") || lowerPath.endsWith(".hpp")) return "C++ Source";
        if (lowerPath.endsWith(".txt")) return "Text Document";
        if (lowerPath.endsWith(".md")) return "Markdown Document";

        return "Unknown Format";
    }

    /**
     * 添加文档到知识库（默认集合）
     */
    public VectorDocument initializeKnowledgeBase(List<String> documentPaths) {
        return initializeKnowledgeBase(documentPaths, null, null);
    }

    /**
     * 添加文档到知识库（指定集合）
     */
    public VectorDocument initializeKnowledgeBase(List<String> documentPaths, String documentSetId, String collectionName) {
        try {
            // 确定目标集合
            String targetCollection = determineCollectionName(
                documentPaths.isEmpty() ? null : documentPaths.get(0),
                collectionName
            );

            log.info("Using collection: {} for document storage", targetCollection);

            List<Document> documents = loadDocuments(documentPaths, documentSetId, targetCollection);

            if (documents.isEmpty()) {
                throw new ServiceException(ExceptionEnum.CM329.getResultCode(), ExceptionEnum.CM329.getResultMsg());
            }

            log.info("Successfully loaded {} documents for collection: {}", documents.size(), targetCollection);

            // 文档切分
            List<TextSegment> segments = splitDocuments(documents);
            log.info("Generated {} text segments for collection: {}", segments.size(), targetCollection);

            // 向量化并存储到指定集合
            return embedAndStore(segments, targetCollection);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the document to the knowledge base", e);
            throw new ServiceException(ExceptionEnum.CM330.getResultCode(), ExceptionEnum.CM330.getResultMsg());
        }
    }

    /**
     * 加载文档
     */
    private List<Document> loadDocuments(List<String> documentPaths, String documentSetId, String collectionName) {
        List<Document> documents = new ArrayList<>();

        int loadedCount = 0;
        int skippedCount = 0;

        for (String path : documentPaths) {
            try {
                // 检查文件是否存在
                if (!Files.exists(Paths.get(path))) {
                    log.warn("✗ File not found: {}", path);
                    skippedCount++;
                    continue;
                }

                // 检查文件格式是否支持
                if (!isSupportedFormat(path)) {
                    log.warn("✗ Unsupported document format: {} ({})", path, getFileFormatDescription(path));
                    skippedCount++;
                    continue;
                }

                Path filePath = Paths.get(path);
                Document document;

                if (path.toLowerCase().endsWith(".pdf")) {
                    // PDF 文件使用 PDF 解析器
                    ApachePdfBoxDocumentParser pdfParser = new ApachePdfBoxDocumentParser();
                    document = FileSystemDocumentLoader.loadDocument(filePath, pdfParser);
                } else if (isTextFormat(path)) {
                    // 所有文本文件使用 TextDocumentParser
                    document = FileSystemDocumentLoader.loadDocument(filePath, new TextDocumentParser());
                } else {
                    log.warn("✗ Unhandled document format: {} ({})", path, getFileFormatDescription(path));
                    skippedCount++;
                    continue;
                }

                // 添加元数据
                if (documentSetId != null) {
                    document.metadata().put("documentSetId", documentSetId);
                }
                document.metadata().put("source", path);
                document.metadata().put("format", getFileFormatDescription(path));
                document.metadata().put("timestamp", String.valueOf(System.currentTimeMillis()));
                document.metadata().put("collection", collectionName); // 添加集合信息

                documents.add(document);
                loadedCount++;
                log.info("✓ Loaded document: {} ({}) to collection: {}",
                    path, getFileFormatDescription(path), collectionName);

            } catch (Exception e) {
                log.error("✗ Failed to load the document: {} - {}", path, e.getMessage());
                skippedCount++;
            }
        }

        log.info("Document loading summary: {} loaded, {} skipped, {} total paths for collection: {}",
            loadedCount, skippedCount, documentPaths.size(), collectionName);

        return documents;
    }

    /**
     * 文档切分
     */
    private List<TextSegment> splitDocuments(List<Document> documents) {
        DocumentSplitter splitter = DocumentSplitters.recursive(
            ragConfig.getChunkSize(),
            ragConfig.getChunkOverlap()
        );
        return splitter.splitAll(documents);
    }

    /**
     * 向量化并存储到指定集合
     */
    private VectorDocument embedAndStore(List<TextSegment> segments, String collectionName) {
        log.info("Begin vectorized storage to collection: {}...", collectionName);
        long startTime = System.currentTimeMillis();

        int successCount = 0;
        int errorCount = 0;

        // 批量处理，提高性能
        int batchSize = 50;
        for (int i = 0; i < segments.size(); i += batchSize) {
            int end = Math.min(i + batchSize, segments.size());
            List<TextSegment> batch = segments.subList(i, end);

            BatchResult result = processBatch(batch, i, segments.size(), collectionName);
            successCount += result.getSuccessCount();
            errorCount += result.getErrorCount();
        }

        long endTime = System.currentTimeMillis();
        log.info("Vectorization completed in collection {}: {} successful, {} failed, time taken: {} ms",
            collectionName, successCount, errorCount, (endTime - startTime));

        return new VectorDocument(successCount, errorCount, null, collectionName);
    }

    /**
     * 处理批次数据
     */
    private BatchResult processBatch(List<TextSegment> batch, int startIndex, int totalSize, String collectionName) {
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
                    log.info("Processed {}/{} text segments for collection: {}",
                        (startIndex + i + 1), totalSize, collectionName);
                }
            } catch (Exception e) {
                errorCount++;
                log.error("Vectorization failed [{}] in collection {}: {}",
                    (startIndex + i + 1), collectionName,
                    segment.text().substring(0, Math.min(100, segment.text().length())));
            }
        }

        if (!embeddings.isEmpty()) {
            try {
                embeddingStore.addAll(embeddings, segmentsToStore);
                log.debug("Successfully stored {} text segments to vector database in collection: {}",
                    embeddings.size(), collectionName);
            } catch (Exception e) {
                log.error("Batch storage to vector database failed in collection: {}", collectionName, e);
                errorCount += embeddings.size();
                successCount -= embeddings.size();
            }
        }
        BatchResult result = new BatchResult();
        result.setSuccessCount(successCount);
        result.setErrorCount(errorCount);
        return result;
    }

    /**
     * 在指定集合中检索
     */
    public List<EmbeddingMatchDto> search(SearchRequest searchDto) {
        try {
            Embedding queryEmbedding = embeddingModel.embed(searchDto.getContent()).content();

            EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(searchDto.getMaxResults())
                .minScore(searchDto.getMinScore())
                .build();

            List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(searchRequest).matches();

            // 如果指定了集合名称，进行过滤
            if (searchDto.getCollection() != null) {
                matches = filterByCollection(matches, searchDto.getCollection());
            }

            // 转换为 DTO
            List<EmbeddingMatchDto> results = matches.stream()
                .map(EmbeddingMatchDto::from)
                .collect(Collectors.toList());

            log.info("Retrieved {} related documents from collection: {}", results.size(),
                searchDto.getCollection() != null ? searchDto.getCollection() : "all collections");
            return results;

        } catch (Exception e) {
            log.error("Retrieval failed", e);
            throw new ServiceException(ExceptionEnum.CM331.getResultCode(), ExceptionEnum.CM331.getResultMsg());
        }
    }

    /**
     * 根据集合名称过滤结果
     */
    private static List<EmbeddingMatch<TextSegment>> filterByCollection(
        List<EmbeddingMatch<TextSegment>> results, String collectionName) {

        List<EmbeddingMatch<TextSegment>> filteredResults = new ArrayList<>();

        for (EmbeddingMatch<TextSegment> match : results) {
            String collection = match.embedded().metadata().getString("collection");
            if (collectionName.equals(collection)) {
                filteredResults.add(match);
            }
        }

        return filteredResults;
    }

    /**
     * 跨集合搜索（在所有集合中搜索）
     */
    public Map<String, List<EmbeddingMatchDto>> searchAcrossCollections(SearchRequest searchDto) {
        Map<String, List<EmbeddingMatchDto>> results = new HashMap<>();

        for (String collection : SUPPORTED_COLLECTIONS) {
            try {
                searchDto.setCollection(collection);
                List<EmbeddingMatchDto> collectionResults = search(searchDto);
                results.put(collection, collectionResults);
                log.info("Found {} results in collection: {}", collectionResults.size(), collection);
            } catch (Exception e) {
                log.warn("Search failed in collection: {}", collection, e);
                results.put(collection, new ArrayList<>());
            }
        }

        return results;
    }

    /**
     * 根据文件路径删除指定集合中的文档
     */
    public DeleteResult deleteByFilePath(String filePath, String collectionName) {
        try {
            log.info("Deleting documents by file path: {} from collection: {}",
                filePath, collectionName != null ? collectionName : "all collections");
            long startTime = System.currentTimeMillis();

            // 搜索包含该文件路径的所有向量
            List<EmbeddingMatch<TextSegment>> matches = searchBySource(filePath, collectionName);

            if (matches.isEmpty()) {
                log.warn("No documents found for file path: {} in collection: {}",
                    filePath, collectionName != null ? collectionName : "any collection");
                return new DeleteResult(0, 0, filePath);
            }

            // 提取要删除的向量ID
            List<String> idsToRemove = matches.stream()
                .map(EmbeddingMatch::embeddingId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            int deletedCount = 0;
            if (!idsToRemove.isEmpty()) {
                // 批量删除向量
                embeddingStore.removeAll(idsToRemove);
                deletedCount = idsToRemove.size();
            }

            long endTime = System.currentTimeMillis();
            log.info("Deleted {} vectors for file: {} from collection: {}, time taken: {} ms",
                deletedCount, filePath, collectionName != null ? collectionName : "all collections",
                (endTime - startTime));

            return new DeleteResult(deletedCount, 0, filePath);

        } catch (Exception e) {
            log.error("Failed to delete documents by file path: {} from collection: {}",
                filePath, collectionName, e);
            throw new ServiceException(ExceptionEnum.CM332.getResultCode(), "Delete document failed");
        }
    }

    /**
     * 根据源文件路径搜索向量（支持集合过滤）
     */
    private List<EmbeddingMatch<TextSegment>> searchBySource(String sourcePath, String collectionName) {
        try {
            // 使用更合理的查询文本
            String queryText = "document content analysis";

            EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(embeddingModel.embed(queryText).content())
                    .maxResults(1000)
                    .minScore(0.1)
                    .build();

            List<EmbeddingMatch<TextSegment>> allMatches = embeddingStore.search(searchRequest).matches();

            // 在应用层过滤
            return allMatches.stream()
                    .filter(match -> {
                        String source = match.embedded().metadata().getString("source");
                        String collection = match.embedded().metadata().getString("collection");

                        boolean sourceMatch = source != null && source.equals(sourcePath);
                        boolean collectionMatch = collectionName == null ||
                                (collection != null && collection.equals(collectionName));

                        return sourceMatch && collectionMatch;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Failed to search vectors by source: {} in collection: {}", sourcePath, collectionName, e);
            return new ArrayList<>();
        }
    }

    /**
     * 批量删除多个文件（默认集合）
     */
    public BatchDeleteResult deleteMultipleFiles(List<String> filePaths) {
        return deleteMultipleFiles(filePaths, null);
    }

    /**
     * 批量删除多个文件（指定集合）
     */
    public BatchDeleteResult deleteMultipleFiles(List<String> filePaths, String collectionName) {
        try {
            log.info("Deleting multiple files: {} from collection: {}", filePaths,
                    collectionName != null ? collectionName : "all collections");
            long startTime = System.currentTimeMillis();

            int totalDeleted = 0;
            int totalFailed = 0;
            List<DeleteResult> results = new ArrayList<>();

            for (String filePath : filePaths) {
                try {
                    DeleteResult result = deleteByFilePath(filePath, collectionName);
                    results.add(result);
                    totalDeleted += result.getDeletedCount();
                    if (result.getFailedCount() > 0) {
                        totalFailed += result.getFailedCount();
                    }
                } catch (Exception e) {
                    log.error("Failed to delete file: {} from collection: {}", filePath, collectionName, e);
                    totalFailed++;
                    results.add(new DeleteResult(0, 1, filePath));
                }
            }

            long endTime = System.currentTimeMillis();
            log.info("Batch deletion completed: {} deleted, {} failed, time taken: {} ms from collection: {}",
                totalDeleted, totalFailed, (endTime - startTime),
                collectionName != null ? collectionName : "all collections");

            return new BatchDeleteResult(totalDeleted, totalFailed, results);

        } catch (Exception e) {
            log.error("Failed to delete multiple files from collection: {}", collectionName, e);
            throw new ServiceException(ExceptionEnum.CM332.getResultCode(), "Batch delete files failed");
        }
    }
    /**
     * 获取所有集合及其包含的文档路径
     */
    public Map<String, List<String>> getAllCollectionDocuments() {
        Map<String, List<String>> collectionDocuments = new HashMap<>();
        try {
            // 遍历所有支持的集合
            for (String collection : SUPPORTED_COLLECTIONS) {
                List<String> documents = getStoredFiles(collection);
                if (!documents.isEmpty()) {
                    collectionDocuments.put(collection, documents);
                }
            }

            log.info("Retrieved documents from {} collections", collectionDocuments.size());
            return collectionDocuments;

        } catch (Exception e) {
            log.error("Failed to get collection documents", e);
            return collectionDocuments;
        }
    }

    /**
     * 获取指定集合的文档路径
     */
    public Map<String, List<String>> getCollectionDocuments(String collectionName) {
        Map<String, List<String>> result = new HashMap<>();
        try {
            if (!isValidCollection(collectionName)) {
                throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "Invalid collection name: " + collectionName);
            }

            List<String> documents = getStoredFiles(collectionName);

            result.put(collectionName, documents);

            log.info("Retrieved {} documents from collection: {}", documents.size(), collectionName);
            return result;

        } catch (Exception e) {
            log.error("Failed to get documents from collection: {}", collectionName, e);
            return result;
        }
    }

    /**
     * 获取所有已存储的文件列表（支持集合过滤）
     */
    public List<String> getStoredFiles() {
        return getStoredFiles(null);
    }

    /**
     * 获取指定集合中已存储的文件列表
     */
    public List<String> getStoredFiles(String collectionName) {
        try {
            EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddingModel.embed("test").content())
                .maxResults(10000)
                .minScore(0.0)
                .build();

            List<EmbeddingMatch<TextSegment>> allMatches = embeddingStore.search(searchRequest).matches();

            // 提取所有唯一的源文件路径（支持集合过滤）
            return allMatches.stream()
                .filter(match -> {
                    String collection = match.embedded().metadata().getString("collection");
                    return collectionName == null ||
                                (collection != null && collection.equals(collectionName));
                })
                    .map(match -> match.embedded().metadata().getString("source"))
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Failed to get stored files list for collection: {}", collectionName, e);
            return new ArrayList<>();
        }
    }


    /**
     * 获取文档集列表（支持集合过滤）
     */
    public List<String> getDocumentSets() {
        return getDocumentSets(null);
    }

    /**
     * 获取指定集合中的文档集列表
     */
    public List<String> getDocumentSets(String collectionName) {
        try {
            EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddingModel.embed("test").content())
                .maxResults(10000)
                .minScore(0.0)
                .build();

            List<EmbeddingMatch<TextSegment>> allMatches = embeddingStore.search(searchRequest).matches();

            // 提取所有唯一的文档集ID（支持集合过滤）
            return allMatches.stream()
                .filter(match -> {
                    String collection = match.embedded().metadata().getString("collection");
                    return collectionName == null ||
                        (collection != null && collection.equals(collectionName));
                })
                    .map(match -> match.embedded().metadata().getString("documentSetId"))
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Failed to get document sets list for collection: {}", collectionName, e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取支持的集合列表
     */
    public List<String> getSupportedCollections() {
        return new ArrayList<>(SUPPORTED_COLLECTIONS);
    }

    /**
     * 获取集合统计信息
     */
    public Map<String, Integer> getCollectionStats() {
        Map<String, Integer> stats = new HashMap<>();

        for (String collection : SUPPORTED_COLLECTIONS) {
            try {
                List<String> files = getStoredFiles(collection);
                stats.put(collection, files.size());
                log.debug("Collection {} has {} files", collection, files.size());
            } catch (Exception e) {
                log.warn("Failed to get stats for collection: {}", collection, e);
                stats.put(collection, 0);
            }
        }

        return stats;
    }

    /**
     * 清空指定集合
     */
    public void clearCollection(String collectionName) {
        try {
            if (!isValidCollection(collectionName)) {
                throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "Invalid collection name: " + collectionName);
            }

            // 获取该集合中的所有文档并删除
            List<String> files = getStoredFiles(collectionName);
            if (!files.isEmpty()) {
                deleteMultipleFiles(files, collectionName);
            }

            log.info("Collection cleared successfully: {}", collectionName);
        } catch (Exception e) {
            log.error("Failed to clear collection: {}", collectionName, e);
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "Clear collection failed: " + collectionName);
        }
    }

    /**
     * 清空向量库（所有集合）
     */
    public void clearVectorStore() {
        try {
            embeddingStore.removeAll();
            log.info("Vector store cleared successfully (all collections)");

            log.info("Vector store cleared successfully (all collections)");
        } catch (Exception e) {
            log.error("Failed to clear vector library", e);
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "Clear vector store failed");
        }
    }

}

