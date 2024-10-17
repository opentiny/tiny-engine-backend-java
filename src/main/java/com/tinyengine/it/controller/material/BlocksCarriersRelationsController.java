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
 * @since 2024-04-19
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/blocks-carriers-relations")
public class BlocksCarriersRelationsController {
    @Autowired
    BlocksCarriersRelationsService blocksCarriersRelationsService;

    @GetMapping("/")
    public Result<List<BlocksCarriersRelations>> getAllBlocksCarriersRelations() {
        List<BlocksCarriersRelations> BlocksCarriersRelationsList = new ArrayList<BlocksCarriersRelations>();
        BlocksCarriersRelationsList = blocksCarriersRelationsService.findAllBlocksCarriersRelations();
        return Result.success(BlocksCarriersRelationsList);
    }

    @GetMapping("/{id}")
    public Result<BlocksCarriersRelations> getBlocksCarriersRelationsById(@PathVariable Integer id) {
        BlocksCarriersRelations blocksCarriersRelations = blocksCarriersRelationsService.findBlocksCarriersRelationsById(id);
        return Result.success(blocksCarriersRelations);
    }

    @PostMapping("/")
    public Result<BlocksCarriersRelations> createBlocksCarriersRelations(@Valid @RequestBody BlocksCarriersRelations blocksCarriersRelations) {
        blocksCarriersRelationsService.createBlocksCarriersRelations(blocksCarriersRelations);
        int id = blocksCarriersRelations.getId();
        blocksCarriersRelations = blocksCarriersRelationsService.findBlocksCarriersRelationsById(id);
        return Result.success(blocksCarriersRelations);
    }

    @PutMapping("/{id}")
    public Result<BlocksCarriersRelations> updateBlocksCarriersRelations(@PathVariable Integer id, @RequestBody BlocksCarriersRelations blocksCarriersRelations) {
        blocksCarriersRelations.setId(id);
        blocksCarriersRelationsService.updateBlocksCarriersRelationsById(blocksCarriersRelations);
        blocksCarriersRelations = blocksCarriersRelationsService.findBlocksCarriersRelationsById(id);
        return Result.success(blocksCarriersRelations);
    }

    @DeleteMapping("/{id}")
    public Result<BlocksCarriersRelations> deleteBlocksCarriersRelations(@PathVariable Integer id) throws ServiceException {
        BlocksCarriersRelations blocksCarriersRelations = blocksCarriersRelationsService.findBlocksCarriersRelationsById(id);
        blocksCarriersRelationsService.deleteBlocksCarriersRelationsById(id);
        return Result.success(blocksCarriersRelations);
    }
}
