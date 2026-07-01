package com.tinyengine.it.modeldata.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "t_api_key")
public class ApiKeyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "api_key")
	@Schema(name = "apiKey", description = "站点ID")
	private String apiKey;
	@Schema(name = "apiSecret", description = "秘钥")
	private String apiSecret;
	@Schema(name = "tenantId", description = "租户id")
	private String tenantId;
	@Schema(name = "status", description = "状态，0-禁用，1-启用")
	private Integer status;
	@Schema(name = "expireTime", description = "过期时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("expire_time")
	private LocalDateTime expireTime;
}
