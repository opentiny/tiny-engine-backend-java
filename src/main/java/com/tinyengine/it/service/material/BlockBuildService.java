package com.tinyengine.it.service.material;

import com.tinyengine.it.model.dto.BlockBuildDto;
import com.tinyengine.it.model.entity.BlockHistories;
import com.tinyengine.it.model.entity.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BlockBuildService {
	/**
	 * 区块发布
	 */
	CompletableFuture<Void> start(int blockId, int taskId, BlockBuildDto blockBuildDto);

	int ensureBlockId(Blocks blocks);

	List<BlockHistories> isHistoryExisted(Integer id, String version);
}
