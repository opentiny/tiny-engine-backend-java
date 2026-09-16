/**
 * Copyright (c) 2023 - present TinyEngine Authors. Copyright (c) 2023 - present Huawei Cloud
 * Computing Technologies Co., Ltd.
 *
 * <p>Use of this source code is governed by an MIT-style license.
 *
 * <p>THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR A
 * PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */
package com.tinyengine.it.rag.service;

import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.rag.config.RAGConfig;
import com.tinyengine.it.rag.entity.BatchDeleteResult;
import com.tinyengine.it.rag.entity.BatchResult;
import com.tinyengine.it.rag.entity.DeleteResult;
import com.tinyengine.it.rag.entity.EmbeddingMatchDto;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** 存储服务 - 支持动态集合管理 */
@Slf4j
@Service
@SuppressWarnings({
    "PMD.AvoidCatchingGenericException",
    "PMD.AvoidDuplicateLiterals",
    "PMD.AvoidInstantiatingObjectsInLoops",
    "PMD.AvoidRethrowingException",
    "PMD.CommentDefaultAccessModifier",
    "PMD.CyclomaticComplexity",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.DefaultPackage",
    "PMD.ExcessiveClassLength",
    "PMD.ExcessiveImports",
    "PMD.FieldDeclarationsShouldBeAtStartOfClass",
    "PMD.GodClass",
    "PMD.LawOfDemeter",
    "PMD.LocalVariableCouldBeFinal",
    "PMD.LongVariable",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.OnlyOneReturn",
    "PMD.PrematureDeclaration",
    "PMD.PreserveStackTrace",
    "PMD.TooManyMethods",
    "PMD.UselessParentheses"
})
public class StorageService {
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    private final RAGConfig ragConfig;

    // 支持的集合列表
    private static final List<String> VALID_COLLECTIONS =
            List.of("tinyengine_documents", "agent_documents");
    private static final int STORE_BATCH_SIZE = 50;
    private static final int LOG_INTERVAL = 100;
    private static final int PREVIEW_LEN = 100;
    private static final int SOURCE_SCAN_LIMIT = 1000;
    private static final double SOURCE_MIN_SCORE = 0.1;
    private static final int LIST_SCAN_LIMIT = 10_000;

    // 默认集合
    private static final String DEFAULT_COLL = "tinyengine_documents";

    // 集合映射配置
    private final Map<String, String> collectionMapping = new HashMap<>();

    private static void logDebug(final String message, final Object... arguments) {
        if (log.isDebugEnabled()) {
            log.debug(message, arguments);
        }
    }

    private static void logInfo(final String message, final Object... arguments) {
        if (log.isInfoEnabled()) {
            log.info(message, arguments);
        }
    }

    private static void logWarn(final String message, final Object... arguments) {
        if (log.isWarnEnabled()) {
            log.warn(message, arguments);
        }
    }

    private static void logError(final String message, final Object... arguments) {
        if (log.isErrorEnabled()) {
            log.error(message, arguments);
        }
    }

    /** 支持的文档格式 */
    private static final List<String> SUPPORTED_FORMATS =
            List.of(
                    ".pdf",
                    ".txt",
                    ".md",
                    ".sql",
                    ".java",
                    ".py",
                    ".js",
                    ".ts",
                    ".html",
                    ".css",
                    ".xml",
                    ".json",
                    ".yaml",
                    ".yml",
                    ".properties",
                    ".sh",
                    ".bat",
                    ".cmd",
                    ".c",
                    ".cpp",
                    ".h",
                    ".hpp");

    /** 文本文件格式（使用 TextDocumentParser） */
    private static final List<String> TEXT_FORMATS =
            List.of(
                    ".txt",
                    ".md",
                    ".sql",
                    ".java",
                    ".py",
                    ".js",
                    ".ts",
                    ".html",
                    ".css",
                    ".xml",
                    ".json",
                    ".yaml",
                    ".yml",
                    ".properties",
                    ".sh",
                    ".bat",
                    ".cmd",
                    ".c",
                    ".cpp",
                    ".h",
                    ".hpp");

    private static final Map<String, String> FORMAT_DESC =
            Map.ofEntries(
                    Map.entry(".pdf", "PDF Document"),
                    Map.entry(".sql", "SQL Script"),
                    Map.entry(".java", "Java Source"),
                    Map.entry(".py", "Python Script"),
                    Map.entry(".js", "JavaScript"),
                    Map.entry(".ts", "TypeScript"),
                    Map.entry(".html", "HTML Document"),
                    Map.entry(".css", "CSS Stylesheet"),
                    Map.entry(".xml", "XML Document"),
                    Map.entry(".json", "JSON Data"),
                    Map.entry(".yaml", "YAML Configuration"),
                    Map.entry(".yml", "YAML Configuration"),
                    Map.entry(".properties", "Properties File"),
                    Map.entry(".sh", "Shell Script"),
                    Map.entry(".bat", "Batch File"),
                    Map.entry(".cmd", "Batch File"),
                    Map.entry(".c", "C Source"),
                    Map.entry(".cpp", "C++ Source"),
                    Map.entry(".h", "C++ Source"),
                    Map.entry(".hpp", "C++ Source"),
                    Map.entry(".txt", "Text Document"),
                    Map.entry(".md", "Markdown Document"));

    /**
     * Check if file format is supported.
     *
     * @return whether the file format is supported
     */
    private boolean isSupportedFormat(Path filePath) {
        Path fileNamePath = filePath == null ? null : filePath.getFileName();
        if (fileNamePath == null) {
            return false;
        }

        String fileName = fileNamePath.toString().toLowerCase(Locale.ROOT);
        return SUPPORTED_FORMATS.stream().anyMatch(format -> fileName.endsWith(format));
    }

    Path getDocumentRoot() {
        String rootPath = ragConfig.getDocumentRoot();
        if (rootPath == null || rootPath.isBlank()) {
            throw new ServiceException(
                    ExceptionEnum.CM329.getResultCode(), "Document root is not configured");
        }

        try {
            Path root = Paths.get(rootPath).toAbsolutePath().normalize();
            if (!Files.exists(root) || !Files.isDirectory(root)) {
                throw new ServiceException(
                        ExceptionEnum.CM329.getResultCode(),
                        "Document root does not exist: " + root);
            }
            return root.toRealPath();
        } catch (InvalidPathException | IOException e) {
            throw new ServiceException(
                    ExceptionEnum.CM329.getResultCode(), "Invalid document root: " + rootPath);
        }
    }

    Path resolveDocumentPath(String rawPath) {
        return resolveDocumentPath(rawPath, getDocumentRoot());
    }

    Path resolveDocumentPath(String rawPath, Path documentRoot) {
        if (rawPath == null || rawPath.isBlank()) {
            throw new ServiceException(
                    ExceptionEnum.CM329.getResultCode(), "Document path cannot be empty");
        }

        try {
            Path root = documentRoot.toAbsolutePath().normalize();
            Path requested = Paths.get(rawPath);
            Path resolved =
                    requested.isAbsolute()
                            ? requested.toAbsolutePath().normalize()
                            : root.resolve(requested).normalize();
            if (!resolved.startsWith(root)) {
                throw new ServiceException(
                        ExceptionEnum.CM329.getResultCode(),
                        "Document path is outside the allowed root");
            }
            return resolved;
        } catch (InvalidPathException e) {
            throw new ServiceException(
                    ExceptionEnum.CM329.getResultCode(), "Invalid document path");
        }
    }

    private Path resolveRealDocumentPath(Path filePath, Path documentRoot) throws IOException {
        Path root = documentRoot.toRealPath();
        Path realPath = filePath.toRealPath();
        if (!realPath.startsWith(root)) {
            throw new ServiceException(
                    ExceptionEnum.CM329.getResultCode(),
                    "Document path is outside the allowed root");
        }
        return realPath;
    }

    /** 构造函数 */
    public StorageService(
            EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this(embeddingModel, embeddingStore, new RAGConfig());
    }

    @Autowired
    public StorageService(
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore,
            RAGConfig ragConfig) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.ragConfig = ragConfig == null ? new RAGConfig() : ragConfig;
        logInfo(
                "StorageService initialized with support for {} file formats",
                SUPPORTED_FORMATS.size());

        // 初始化集合映射
        initializeCollectionMapping();
    }

    /** 初始化集合映射配置 */
    private void initializeCollectionMapping() {
        // 配置特定文件类型到集合的映射
        collectionMapping.put("agent", "agent_documents");
        collectionMapping.put("tinyengine", "tinyengine_documents");

        logInfo("Collection mapping initialized: {}", collectionMapping);
    }

    /**
     * 检查集合名称是否有效.
     *
     * @return whether the collection name is valid
     */
    private boolean isValidCollection(String collectionName) {
        return VALID_COLLECTIONS.contains(collectionName);
    }

    /**
     * 自动扫描文件夹并添加文档到知识库.
     *
     * @return vector storage result
     */
    public VectorDocument autoAddFolderToKnowledgeBase() {
        try {
            Path folder = getDocumentRoot();

            // 扫描文件夹中的所有支持的文件
            List<String> filePaths = scanSupportedFiles(folder);

            if (filePaths.isEmpty()) {
                throw new ServiceException(
                        ExceptionEnum.CM329.getResultCode(),
                        "No supported file formats found in folder. Supported formats: "
                                + String.join(", ", SUPPORTED_FORMATS));
            }

            logInfo("Found {} supported files in folder: {}", filePaths.size(), folder);

            return initializeKnowledgeBase(filePaths);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logError("Failed to auto add folder to knowledge base", e);
            throw new ServiceException(
                    ExceptionEnum.CM330.getResultCode(),
                    "Auto add folder failed: " + e.getMessage());
        }
    }

    /**
     * 扫描文件夹中所有支持的文件.
     *
     * @return supported file paths
     */
    private List<String> scanSupportedFiles(Path folder) {
        try (Stream<Path> pathStream = Files.walk(folder)) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .filter(this::isSupportedFormat)
                    .peek(filePath -> logDebug("Found supported file: {}", filePath))
                    .map(filePath -> filePath.toAbsolutePath().normalize().toString())
                    .sorted()
                    .collect(Collectors.toList());

        } catch (IOException e) {
            logError("Failed to scan folder: {}", folder, e);
            throw new ServiceException(
                    ExceptionEnum.CM333.getResultCode(), ExceptionEnum.CM333.getResultMsg());
        }
    }

    /**
     * 根据文档路径和自定义集合确定目标集合.
     *
     * @return target collection name
     */
    private String determineCollectionName(String filePath, String customCollection) {
        // 如果指定了自定义集合，优先使用
        if (customCollection != null && !customCollection.isBlank()) {
            if (!isValidCollection(customCollection)) {
                logWarn(
                        "Invalid collection specified: {}, using default: {}",
                        customCollection,
                        DEFAULT_COLL);
                return DEFAULT_COLL;
            }
            return customCollection;
        }

        // 根据文件路径自动判断集合
        if (filePath != null) {
            String lowerPath = filePath.toLowerCase(Locale.ROOT);
            // 如果路径包含特定关键词，映射到对应集合
            for (Map.Entry<String, String> entry : collectionMapping.entrySet()) {
                if (lowerPath.contains(entry.getKey())) {
                    logInfo("Auto-mapped file {} to collection: {}", filePath, entry.getValue());
                    return entry.getValue();
                }
            }
        }

        // 默认集合
        return DEFAULT_COLL;
    }

    /**
     * 检查是否为文本格式.
     *
     * @return whether the file is text format
     */
    private boolean isTextFormat(String filePath) {
        if (filePath == null) {
            return false;
        }

        String lowerPath = filePath.toLowerCase(Locale.ROOT);
        return TEXT_FORMATS.stream().anyMatch(lowerPath::endsWith);
    }

    /**
     * 获取文件格式描述.
     *
     * @return file format description
     */
    private String getFileFormatDescription(String filePath) {
        String description = "unknown";
        if (filePath != null) {
            description = "Unknown Format";
            String lowerPath = filePath.toLowerCase(Locale.ROOT);
            for (Map.Entry<String, String> entry : FORMAT_DESC.entrySet()) {
                if (lowerPath.endsWith(entry.getKey())) {
                    description = entry.getValue();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 添加文档到知识库（默认集合）.
     *
     * @return vector storage result
     */
    public VectorDocument initializeKnowledgeBase(List<String> documentPaths) {
        return initializeKnowledgeBase(documentPaths, null, null);
    }

    /**
     * 添加文档到知识库（指定集合）.
     *
     * @return vector storage result
     */
    public VectorDocument initializeKnowledgeBase(
            List<String> documentPaths, String documentSetId, String collectionName) {
        try {
            if (documentPaths == null || documentPaths.isEmpty()) {
                throw new ServiceException(
                        ExceptionEnum.CM329.getResultCode(), ExceptionEnum.CM329.getResultMsg());
            }
            // 确定目标集合
            String targetCollection =
                    determineCollectionName(
                            documentPaths.isEmpty() ? null : documentPaths.get(0), collectionName);

            logInfo("Using collection: {} for document storage", targetCollection);

            List<Document> documents =
                    loadDocuments(documentPaths, documentSetId, targetCollection);

            if (documents.isEmpty()) {
                throw new ServiceException(
                        ExceptionEnum.CM329.getResultCode(), ExceptionEnum.CM329.getResultMsg());
            }

            logInfo(
                    "Successfully loaded {} documents for collection: {}",
                    documents.size(),
                    targetCollection);

            // 文档切分
            List<TextSegment> segments = splitDocuments(documents);
            logInfo(
                    "Generated {} text segments for collection: {}",
                    segments.size(),
                    targetCollection);

            // 向量化并存储到指定集合
            return embedAndStore(segments, targetCollection);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logError("Failed to add the document to the knowledge base", e);
            throw new ServiceException(
                    ExceptionEnum.CM330.getResultCode(), ExceptionEnum.CM330.getResultMsg());
        }
    }

    /**
     * 加载文档.
     *
     * @return loaded documents
     */
    private List<Document> loadDocuments(
            List<String> documentPaths, String documentSetId, String collectionName) {
        List<Document> documents = new ArrayList<>();
        Path documentRoot = getDocumentRoot();

        int loadedCount = 0;
        int skippedCount = 0;

        for (String path : documentPaths) {
            try {
                Path filePath = resolveDocumentPath(path, documentRoot);
                // 检查文件是否存在
                if (!Files.exists(filePath)) {
                    logWarn("✗ File not found: {}", path);
                    skippedCount++;
                    continue;
                }
                filePath = resolveRealDocumentPath(filePath, documentRoot);

                // 检查文件格式是否支持
                if (!isSupportedFormat(filePath)) {
                    logWarn(
                            "✗ Unsupported document format: {} ({})",
                            path,
                            getFileFormatDescription(path));
                    skippedCount++;
                    continue;
                }

                Document document;

                if (filePath.toString().toLowerCase(Locale.ROOT).endsWith(".pdf")) {
                    // PDF 文件使用 PDF 解析器
                    ApachePdfBoxDocumentParser pdfParser = new ApachePdfBoxDocumentParser();
                    document = FileSystemDocumentLoader.loadDocument(filePath, pdfParser);
                } else if (isTextFormat(filePath.toString())) {
                    // 所有文本文件使用 TextDocumentParser
                    document =
                            FileSystemDocumentLoader.loadDocument(
                                    filePath, new TextDocumentParser());
                } else {
                    logWarn(
                            "✗ Unhandled document format: {} ({})",
                            path,
                            getFileFormatDescription(path));
                    skippedCount++;
                    continue;
                }

                // 添加元数据
                if (documentSetId != null) {
                    document.metadata().put("documentSetId", documentSetId);
                }
                document.metadata().put("source", filePath.toString());
                document.metadata().put("format", getFileFormatDescription(filePath.toString()));
                document.metadata().put("timestamp", String.valueOf(System.currentTimeMillis()));
                document.metadata().put("collection", collectionName); // 添加集合信息

                documents.add(document);
                loadedCount++;
                logInfo(
                        "✓ Loaded document: {} ({}) to collection: {}",
                        filePath,
                        getFileFormatDescription(filePath.toString()),
                        collectionName);

            } catch (Exception e) {
                logError("✗ Failed to load the document: {} - {}", path, e.getMessage());
                skippedCount++;
            }
        }

        logInfo(
                "Document loading summary: {} loaded, {} skipped, {} total paths for collection:"
                        + " {}",
                loadedCount,
                skippedCount,
                documentPaths.size(),
                collectionName);

        return documents;
    }

    /**
     * 文档切分.
     *
     * @return text segments
     */
    private List<TextSegment> splitDocuments(List<Document> documents) {
        DocumentSplitter splitter =
                DocumentSplitters.recursive(ragConfig.getChunkSize(), ragConfig.getChunkOverlap());
        return splitter.splitAll(documents);
    }

    /**
     * 向量化并存储到指定集合.
     *
     * @return vector storage result
     */
    private VectorDocument embedAndStore(List<TextSegment> segments, String collectionName) {
        logInfo("Begin vectorized storage to collection: {}...", collectionName);
        long startTime = System.currentTimeMillis();

        int successCount = 0;
        int errorCount = 0;

        // 批量处理，提高性能
        int batchSize = STORE_BATCH_SIZE;
        for (int i = 0; i < segments.size(); i += batchSize) {
            int end = Math.min(i + batchSize, segments.size());
            List<TextSegment> batch = segments.subList(i, end);

            BatchResult result = processBatch(batch, i, segments.size(), collectionName);
            successCount += result.getSuccessCount();
            errorCount += result.getErrorCount();
        }

        long endTime = System.currentTimeMillis();
        logInfo(
                "Vectorization completed in collection {}: {} successful, {} failed, time taken: {}"
                        + " ms",
                collectionName,
                successCount,
                errorCount,
                (endTime - startTime));

        return new VectorDocument(successCount, errorCount, null, collectionName);
    }

    /**
     * 处理批次数据.
     *
     * @return batch processing result
     */
    private BatchResult processBatch(
            List<TextSegment> batch, int startIndex, int totalSize, String collectionName) {
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

                if ((startIndex + i + 1) % LOG_INTERVAL == 0) {
                    logInfo(
                            "Processed {}/{} text segments for collection: {}",
                            (startIndex + i + 1),
                            totalSize,
                            collectionName);
                }
            } catch (RuntimeException exception) {
                errorCount++;
                logError(
                        "Vectorization failed [{}] in collection {}: {}",
                        (startIndex + i + 1),
                        collectionName,
                        segment.text()
                                .substring(0, Math.min(PREVIEW_LEN, segment.text().length())),
                        exception);
            }
        }

        if (!embeddings.isEmpty()) {
            try {
                embeddingStore.addAll(embeddings, segmentsToStore);
                logDebug(
                        "Successfully stored {} text segments to vector database in collection: {}",
                        embeddings.size(),
                        collectionName);
            } catch (RuntimeException exception) {
                logError(
                        "Batch storage to vector database failed in collection: {}",
                        collectionName,
                        exception);
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
     * 在指定集合中检索.
     *
     * @return matched embedding results
     */
    public List<EmbeddingMatchDto> search(SearchRequest searchDto) {
        try {
            Embedding queryEmbedding = embeddingModel.embed(searchDto.getContent()).content();

            EmbeddingSearchRequest searchRequest =
                    EmbeddingSearchRequest.builder()
                            .queryEmbedding(queryEmbedding)
                            .maxResults(searchDto.getMaxResults())
                            .minScore(searchDto.getMinScore())
                            .build();

            List<EmbeddingMatch<TextSegment>> matches =
                    embeddingStore.search(searchRequest).matches();

            // 如果指定了集合名称，进行过滤
            if (searchDto.getCollection() != null) {
                matches = filterByCollection(matches, searchDto.getCollection());
            }

            // 转换为 DTO
            List<EmbeddingMatchDto> results =
                    matches.stream().map(EmbeddingMatchDto::from).collect(Collectors.toList());

            logInfo(
                    "Retrieved {} related documents from collection: {}",
                    results.size(),
                    searchDto.getCollection() != null
                            ? searchDto.getCollection()
                            : "all collections");
            return results;

        } catch (Exception e) {
            logError("Retrieval failed", e);
            throw new ServiceException(
                    ExceptionEnum.CM331.getResultCode(), ExceptionEnum.CM331.getResultMsg());
        }
    }

    /**
     * 根据集合名称过滤结果.
     *
     * @return filtered embedding results
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
     * 跨集合搜索（在所有集合中搜索）.
     *
     * @return search results grouped by collection
     */
    public Map<String, List<EmbeddingMatchDto>> searchAcrossCollections(SearchRequest searchDto) {
        Map<String, List<EmbeddingMatchDto>> results = new HashMap<>();

        for (String collection : VALID_COLLECTIONS) {
            try {
                searchDto.setCollection(collection);
                List<EmbeddingMatchDto> collectionResults = search(searchDto);
                results.put(collection, collectionResults);
                logInfo(
                        "Found {} results in collection: {}", collectionResults.size(), collection);
            } catch (Exception e) {
                logWarn("Search failed in collection: {}", collection, e);
                results.put(collection, new ArrayList<>());
            }
        }

        return results;
    }

    /**
     * 根据文件路径删除指定集合中的文档.
     *
     * @return delete result
     */
    public DeleteResult deleteByFilePath(String filePath, String collectionName) {
        try {
            final Path documentRoot = getDocumentRoot();
            final Path resolvedPath = resolveDocumentPath(filePath, documentRoot);
            final String safeFilePath =
                    Files.exists(resolvedPath)
                            ? resolveRealDocumentPath(resolvedPath, documentRoot).toString()
                            : resolvedPath.toString();
            logInfo(
                    "Deleting documents by file path: {} from collection: {}",
                    safeFilePath,
                    collectionName != null ? collectionName : "all collections");
            long startTime = System.currentTimeMillis();

            // 搜索包含该文件路径的所有向量
            List<EmbeddingMatch<TextSegment>> matches =
                    searchBySource(safeFilePath, collectionName);

            if (matches.isEmpty()) {
                logWarn(
                        "No documents found for file path: {} in collection: {}",
                        safeFilePath,
                        collectionName != null ? collectionName : "any collection");
                return new DeleteResult(0, 0, safeFilePath);
            }

            // 提取要删除的向量ID
            List<String> idsToRemove =
                    matches.stream()
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
            logInfo(
                    "Deleted {} vectors for file: {} from collection: {}, time taken: {} ms",
                    deletedCount,
                    safeFilePath,
                    collectionName != null ? collectionName : "all collections",
                    (endTime - startTime));

            return new DeleteResult(deletedCount, 0, safeFilePath);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logError(
                    "Failed to delete documents by file path: {} from collection: {}",
                    filePath,
                    collectionName,
                    e);
            throw new ServiceException(
                    ExceptionEnum.CM332.getResultCode(), "Delete document failed");
        }
    }

    /**
     * 根据源文件路径搜索向量（支持集合过滤）.
     *
     * @return matched vectors for the source path
     */
    private List<EmbeddingMatch<TextSegment>> searchBySource(
            String sourcePath, String collectionName) {
        try {
            // 使用更合理的查询文本
            String queryText = "document content analysis";

            EmbeddingSearchRequest searchRequest =
                    EmbeddingSearchRequest.builder()
                            .queryEmbedding(embeddingModel.embed(queryText).content())
                            .maxResults(SOURCE_SCAN_LIMIT)
                            .minScore(SOURCE_MIN_SCORE)
                            .build();

            List<EmbeddingMatch<TextSegment>> allMatches =
                    embeddingStore.search(searchRequest).matches();

            // 在应用层过滤
            return allMatches.stream()
                    .filter(
                            match -> {
                                String source = match.embedded().metadata().getString("source");
                                String collection =
                                        match.embedded().metadata().getString("collection");

                                boolean sourceMatch = source != null && source.equals(sourcePath);
                                boolean collectionMatch =
                                        collectionName == null
                                                || (collection != null
                                                        && collection.equals(collectionName));

                                return sourceMatch && collectionMatch;
                            })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logError(
                    "Failed to search vectors by source: {} in collection: {}",
                    sourcePath,
                    collectionName,
                    e);
            return new ArrayList<>();
        }
    }

    /**
     * 批量删除多个文件（默认集合）.
     *
     * @return batch delete result
     */
    public BatchDeleteResult deleteMultipleFiles(List<String> filePaths) {
        return deleteMultipleFiles(filePaths, null);
    }

    /**
     * 批量删除多个文件（指定集合）.
     *
     * @return batch delete result
     */
    public BatchDeleteResult deleteMultipleFiles(List<String> filePaths, String collectionName) {
        try {
            logInfo(
                    "Deleting multiple files: {} from collection: {}",
                    filePaths,
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
                    logError(
                            "Failed to delete file: {} from collection: {}",
                            filePath,
                            collectionName,
                            e);
                    totalFailed++;
                    results.add(new DeleteResult(0, 1, filePath));
                }
            }

            long endTime = System.currentTimeMillis();
            logInfo(
                    "Batch deletion completed: {} deleted, {} failed, time taken: {} ms from"
                            + " collection: {}",
                    totalDeleted,
                    totalFailed,
                    (endTime - startTime),
                    collectionName != null ? collectionName : "all collections");

            return new BatchDeleteResult(totalDeleted, totalFailed, results);

        } catch (Exception e) {
            logError("Failed to delete multiple files from collection: {}", collectionName, e);
            throw new ServiceException(
                    ExceptionEnum.CM332.getResultCode(), "Batch delete files failed");
        }
    }

    /**
     * 获取所有集合及其包含的文档路径.
     *
     * @return documents grouped by collection
     */
    public Map<String, List<String>> getAllCollectionDocuments() {
        Map<String, List<String>> collectionDocuments = new HashMap<>();
        try {
            // 遍历所有支持的集合
            for (String collection : VALID_COLLECTIONS) {
                List<String> documents = getStoredFiles(collection);
                if (!documents.isEmpty()) {
                    collectionDocuments.put(collection, documents);
                }
            }

            logInfo("Retrieved documents from {} collections", collectionDocuments.size());
            return collectionDocuments;

        } catch (Exception e) {
            logError("Failed to get collection documents", e);
            return collectionDocuments;
        }
    }

    /**
     * 获取指定集合的文档路径.
     *
     * @return documents in the collection
     */
    public Map<String, List<String>> getCollectionDocuments(String collectionName) {
        Map<String, List<String>> result = new HashMap<>();
        try {
            if (!isValidCollection(collectionName)) {
                throw new ServiceException(
                        ExceptionEnum.CM001.getResultCode(),
                        "Invalid collection name: " + collectionName);
            }

            List<String> documents = getStoredFiles(collectionName);

            result.put(collectionName, documents);

            logInfo(
                    "Retrieved {} documents from collection: {}", documents.size(), collectionName);
            return result;

        } catch (Exception e) {
            logError("Failed to get documents from collection: {}", collectionName, e);
            return result;
        }
    }

    /**
     * 获取所有已存储的文件列表（支持集合过滤）.
     *
     * @return stored file paths
     */
    public List<String> getStoredFiles() {
        return getStoredFiles(null);
    }

    /**
     * 获取指定集合中已存储的文件列表.
     *
     * @return stored file paths
     */
    public List<String> getStoredFiles(String collectionName) {
        try {
            EmbeddingSearchRequest searchRequest =
                    EmbeddingSearchRequest.builder()
                            .queryEmbedding(embeddingModel.embed("test").content())
                            .maxResults(LIST_SCAN_LIMIT)
                            .minScore(0.0)
                            .build();

            List<EmbeddingMatch<TextSegment>> allMatches =
                    embeddingStore.search(searchRequest).matches();

            // 提取所有唯一的源文件路径（支持集合过滤）
            return allMatches.stream()
                    .filter(
                            match -> {
                                String collection =
                                        match.embedded().metadata().getString("collection");
                                return collectionName == null
                                        || (collection != null
                                                && collection.equals(collectionName));
                            })
                    .map(match -> match.embedded().metadata().getString("source"))
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logError("Failed to get stored files list for collection: {}", collectionName, e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取文档集列表（支持集合过滤）.
     *
     * @return stored document set identifiers
     */
    public List<String> getDocumentSets() {
        return getDocumentSets(null);
    }

    /**
     * 获取指定集合中的文档集列表.
     *
     * @return stored document set identifiers
     */
    public List<String> getDocumentSets(String collectionName) {
        try {
            EmbeddingSearchRequest searchRequest =
                    EmbeddingSearchRequest.builder()
                            .queryEmbedding(embeddingModel.embed("test").content())
                            .maxResults(LIST_SCAN_LIMIT)
                            .minScore(0.0)
                            .build();

            List<EmbeddingMatch<TextSegment>> allMatches =
                    embeddingStore.search(searchRequest).matches();

            // 提取所有唯一的文档集ID（支持集合过滤）
            return allMatches.stream()
                    .filter(
                            match -> {
                                String collection =
                                        match.embedded().metadata().getString("collection");
                                return collectionName == null
                                        || (collection != null
                                                && collection.equals(collectionName));
                            })
                    .map(match -> match.embedded().metadata().getString("documentSetId"))
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logError("Failed to get document sets list for collection: {}", collectionName, e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取支持的集合列表.
     *
     * @return supported collection names
     */
    public List<String> getSupportedCollections() {
        return new ArrayList<>(VALID_COLLECTIONS);
    }

    /**
     * 获取集合统计信息.
     *
     * @return collection statistics
     */
    public Map<String, Integer> getCollectionStats() {
        Map<String, Integer> stats = new HashMap<>();

        for (String collection : VALID_COLLECTIONS) {
            try {
                List<String> files = getStoredFiles(collection);
                stats.put(collection, files.size());
                logDebug("Collection {} has {} files", collection, files.size());
            } catch (Exception e) {
                logWarn("Failed to get stats for collection: {}", collection, e);
                stats.put(collection, 0);
            }
        }

        return stats;
    }

    /** 清空指定集合 */
    public void clearCollection(String collectionName) {
        try {
            if (!isValidCollection(collectionName)) {
                throw new ServiceException(
                        ExceptionEnum.CM001.getResultCode(),
                        "Invalid collection name: " + collectionName);
            }

            // 获取该集合中的所有文档并删除
            List<String> files = getStoredFiles(collectionName);
            if (!files.isEmpty()) {
                deleteMultipleFiles(files, collectionName);
            }

            logInfo("Collection cleared successfully: {}", collectionName);
        } catch (Exception e) {
            logError("Failed to clear collection: {}", collectionName, e);
            throw new ServiceException(
                    ExceptionEnum.CM001.getResultCode(),
                    "Clear collection failed: " + collectionName);
        }
    }

    /** 清空向量库（所有集合） */
    public void clearVectorStore() {
        try {
            embeddingStore.removeAll();
            logInfo("Vector store cleared successfully (all collections)");

            logInfo("Vector store cleared successfully (all collections)");
        } catch (Exception e) {
            logError("Failed to clear vector library", e);
            throw new ServiceException(
                    ExceptionEnum.CM001.getResultCode(), "Clear vector store failed");
        }
    }
}
