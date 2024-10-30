package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 业务类型表，全局
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_business_category")
@Schema(name = "BusinessCategory", description = "业务类型表，全局")
public class BusinessCategory extends BaseEntity {
    @Schema(name = "code", description = "编码")
    private String code;

    @Schema(name = "name", description = "名称")
    private String name;
}
