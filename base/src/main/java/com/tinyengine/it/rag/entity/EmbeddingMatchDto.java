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

package com.tinyengine.it.rag.entity;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * EmbeddingMatch dto
 */
@Data
public class EmbeddingMatchDto {
    /**
     * 向量id
     */
    private String embeddingId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 匹配分数
     */
    private Double score;

    /**
     * 文档内容
     */
    private String content;

    /**
     * 元数据
     */
    private Map<String, String> metadata;

    /**
     * 集合名称
     */
    private String collection;


    /**
     * 来源
     */
    private String source;

    public static EmbeddingMatchDto from(EmbeddingMatch<TextSegment> match) {
        EmbeddingMatchDto dto = new EmbeddingMatchDto();
        dto.setEmbeddingId(match.embeddingId());
        dto.setScore(match.score());
        dto.setCreateTime(LocalDateTime.now());

        // 安全地提取内容
        if (match.embedded() != null) {
            dto.setContent(match.embedded().text());

            // 提取元数据和特定字段
            if (match.embedded().metadata() != null) {
                Map<String, String> metadataMap = new HashMap<>();

                // 提取常用字段
                String collection = match.embedded().metadata().getString("collection");
                String documentSetId = match.embedded().metadata().getString("documentSetId");
                String source = match.embedded().metadata().getString("source");

                dto.setCollection(collection);
                dto.setSource(source);

                // 将常用字段也放入 metadata map
                if (collection != null) metadataMap.put("collection", collection);
                if (documentSetId != null) metadataMap.put("documentSetId", documentSetId);
                if (source != null) metadataMap.put("source", source);

                dto.setMetadata(metadataMap);
            } else {
                dto.setMetadata(new HashMap<>());
            }
        } else {
            dto.setContent("");
            dto.setMetadata(new HashMap<>());
        }

        return dto;
    }
}
