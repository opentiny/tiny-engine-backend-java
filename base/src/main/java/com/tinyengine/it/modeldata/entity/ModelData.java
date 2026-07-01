package com.tinyengine.it.modeldata.entity;

import com.baomidou.mybatisplus.annotation.*;


import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@TableName("t_model_data")
@Entity(name = "t_model_data")
@Schema(name = "ModelData", description = "模型数据表")
public class ModelData  {
	@Schema(name = "modelId", description = "模型id")
	private Integer modelId;
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	@Schema(name = "dataJson", description = "存储数据")
	private Map<String, Object> dataJson;
	@Id
	@Schema(name = "id", description = "主键id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)   // 新增这一行！
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;


	@Version
	@Schema(name = "version", description = "版本号")
	private String version; // 这就是 _version 的数据来源

	@TableField(fill = FieldFill.INSERT)
	@Schema(name = "createdBy", description = "创建人")
	private String createdBy;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(name = "lastUpdatedBy", description = "最后修改人")
	private String lastUpdatedBy;

	@TableField(fill = FieldFill.INSERT)
	@Schema(name = "createdTime", description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("created_at")
	private LocalDateTime createdTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "lastUpdatedTime", description = "更新时间")
	@JsonProperty("updated_at")
	private LocalDateTime lastUpdatedTime;

	@TableField(fill = FieldFill.INSERT)
	@Schema(name = "tenantId", description = "租户ID")
	private String tenantId;

	@TableField(fill = FieldFill.INSERT)
	@Schema(name = "renterId", description = "业务租户ID")
	private String renterId;

	@Schema(name = "siteId", description = "站点ID")
	private String siteId;



}
