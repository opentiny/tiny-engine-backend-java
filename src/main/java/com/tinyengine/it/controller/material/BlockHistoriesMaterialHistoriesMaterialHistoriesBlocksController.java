package com.tinyengine.it.controller.material;


import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.model.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-18
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/block-histories-material-histories-material-histories-blocks")
public class BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksController {
    @Autowired
    BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksService blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService;

    @GetMapping("/")
    public Result<List<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks>> getAllBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks() {
        List<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksList = new ArrayList<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks>();
        BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksList = blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService.findAllBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks();
        return Result.success(BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksList);
    }

    @GetMapping("/{id}")
    public Result<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> getBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(@PathVariable Integer id) {
        BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks = blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService.findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(id);
        return Result.success(blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
    }

    @PostMapping("/")
    public Result<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> createBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks(@Valid @RequestBody BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks) {
        blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService.createBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks(blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
        int id = blockHistoriesMaterialHistoriesMaterialHistoriesBlocks.getId();
        blockHistoriesMaterialHistoriesMaterialHistoriesBlocks = blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService.findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(id);
        return Result.success(blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
    }

    @PutMapping("/{id}")
    public Result<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> updateBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks(@PathVariable Integer id, @RequestBody BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks) {
        blockHistoriesMaterialHistoriesMaterialHistoriesBlocks.setId(id);
        blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService.updateBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
        blockHistoriesMaterialHistoriesMaterialHistoriesBlocks = blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService.findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(id);
        return Result.success(blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
    }

    @DeleteMapping("/{id}")
    public Result<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> deleteBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks(@PathVariable Integer id) throws ServiceException {
        BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks = blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService.findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(id);
        blockHistoriesMaterialHistoriesMaterialHistoriesBlocksService.deleteBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(id);
        return Result.success(blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
    }
}
