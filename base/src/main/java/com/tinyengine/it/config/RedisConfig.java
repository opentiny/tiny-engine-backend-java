package com.tinyengine.it.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	/**
	 * 专门用于 Nonce 存储的 RedisTemplate（Key 和 Value 都存字符串）
	 * 避免乱码，方便 redis-cli 命令行调试查看
	 */
	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(connectionFactory);
		// StringRedisTemplate 默认使用 StringRedisSerializer，无需额外配置
		return template;
	}

	/**
	 * （可选）如果你的业务还需要存对象，可以配置通用的 RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// 使用 StringRedisSerializer 序列化 Key
		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);

		// Value 使用 Jackson 序列化（如果存对象）
		// 这里不重复贴 Jackson 代码，保持配置简洁
		template.afterPropertiesSet();
		return template;
	}

}
