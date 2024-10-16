package com.tinyengine.it.controller.app;


import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.MaterialHistories;
import com.tinyengine.it.service.app.MaterialHistoriesService;
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
 * @since 2024-04-21
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/material-histories")
public class MaterialHistoriesController {
    @Autowired
    MaterialHistoriesService materialHistoriesService;

    @GetMapping("/")
    public Result<List<MaterialHistories>> getAllMaterialHistories() {
        List<MaterialHistories> MaterialHistoriesList = new ArrayList<MaterialHistories>();
        MaterialHistoriesList = materialHistoriesService.findAllMaterialHistories();
        return Result.success(MaterialHistoriesList);
    }

    @GetMapping("/{id}")
    public Result<MaterialHistories> getMaterialHistoriesById(@PathVariable Integer id) {
        MaterialHistories materialHistories = materialHistoriesService.findMaterialHistoriesById(id);
        return Result.success(materialHistories);
    }

    @PostMapping("/")
    public Result<MaterialHistories> createMaterialHistories(@Valid @RequestBody MaterialHistories materialHistories) {
        materialHistoriesService.createMaterialHistories(materialHistories);
        int id = materialHistories.getId();
        materialHistories = materialHistoriesService.findMaterialHistoriesById(id);
        return Result.success(materialHistories);
    }

    @PutMapping("/{id}")
    public Result<MaterialHistories> updateMaterialHistories(@PathVariable Integer id, @RequestBody MaterialHistories materialHistories) {
        materialHistories.setId(id);
        materialHistoriesService.updateMaterialHistoriesById(materialHistories);
        materialHistories = materialHistoriesService.findMaterialHistoriesById(id);
        return Result.success(materialHistories);
    }

    @DeleteMapping("/{id}")
    public Result<MaterialHistories> deleteMaterialHistories(@PathVariable Integer id) throws ServiceException {
        MaterialHistories materialHistories = materialHistoriesService.findMaterialHistoriesById(id);
        materialHistoriesService.deleteMaterialHistoriesById(id);
        return Result.success(materialHistories);
    }
}
