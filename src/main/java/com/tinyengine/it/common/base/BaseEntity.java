
package com.tinyengine.it.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 基础对象
 */
@Getter
@Setter
public class BaseEntity {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdBy", description = "创建人")
    private String createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "tenantId", description = "租户ID")
    private String tenantId;

    @Schema(name = "siteId", description = "站点ID")
    private String siteId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BaseEntity))
            return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(createdBy, that.createdBy)
                && Objects.equals(lastUpdatedBy, that.lastUpdatedBy) && Objects.equals(createdTime, that.createdTime)
                && Objects.equals(lastUpdatedTime, that.lastUpdatedTime) && Objects.equals(tenantId, that.tenantId)
                && Objects.equals(siteId, that.siteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdBy, lastUpdatedBy, createdTime, lastUpdatedTime, tenantId, siteId);
    }
}
