package com.tinyengine.it.modeldata.filter;

import com.tinyengine.it.login.config.context.DefaultLoginUserContext;
import com.tinyengine.it.login.utils.SignatureUtil;
import com.tinyengine.it.modeldata.entity.ApiKeyEntity;
import com.tinyengine.it.modeldata.service.ApiKeyService;
import com.tinyengine.it.modeldata.util.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(1)  // 确保在所有业务过滤器之前
public class ApiKeyAuthFilter  extends OncePerRequestFilter {

	@Autowired
	private ApiKeyService apiKeyService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (!requestURI.startsWith("/platform-center/api/model-data")) {
			filterChain.doFilter(request, response);
			return;
		}
		String org = request.getHeader("X-Lowcode-Org");
        if(org == null || org.isEmpty()) {
			response.setStatus(401);
			response.getWriter().write("Missing required header: X-Lowcode-Org");
			return;
		}
		// 1. 包装请求以支持多次读取 Body
		CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);

		// 2. 提取认证头
		String apiKey = request.getHeader("X-API-Key");
		String timestamp = request.getHeader("X-Timestamp");
		String nonce = request.getHeader("X-Nonce");
		String signature = request.getHeader("X-Signature");

		if (apiKey == null || timestamp == null || nonce == null || signature == null) {
			response.setStatus(401);
			response.getWriter().write("Missing required headers: X-API-Key, X-Timestamp, X-Nonce, X-Signature");
			return;
		}

		// 3. 时间戳有效期校验（5分钟）
		long now = System.currentTimeMillis();
		long reqTime;
		try {
			reqTime = Long.parseLong(timestamp);
		} catch (NumberFormatException e) {
			response.setStatus(401);
			response.getWriter().write("Invalid timestamp");
			return;
		}
		if (Math.abs(now - reqTime) > 300_000) {
			response.setStatus(401);
			response.getWriter().write("Request expired (timestamp out of 5min window)");
			return;
		}

		// 4. Nonce 防重放（Redis 存储，有效期5分钟）
		String nonceKey = "nonce:" + nonce;
		Boolean isNew = stringRedisTemplate.opsForValue().setIfAbsent(nonceKey, "1", Duration.ofMinutes(5));
		if (Boolean.FALSE.equals(isNew)) {
			response.setStatus(401);
			response.getWriter().write("Nonce already used");
			return;
		}

		// 5. 根据 API Key 获取密钥和租户信息
		ApiKeyEntity keyEntity = apiKeyService.getValidKey(apiKey);
		if (keyEntity == null) {
			response.setStatus(401);
			response.getWriter().write("Invalid API Key");
			return;
		}



		// 7. 构建待签名字符串：method\npath\ntimestamp\nnonce\nbody
		String method = request.getMethod();
		String path = request.getRequestURI();
		String body = wrappedRequest.getBodyString();  // 使用缓存中的 body
		String stringToSign = String.format("%s\n%s\n%s\n%s\n%s", method, path, timestamp, nonce, body);

		// 8. 计算服务端签名并比对
		String expectedSignature = SignatureUtil.hmacSha256(keyEntity.getApiSecret(), stringToSign);
		if (!expectedSignature.equals(signature)) {
			response.setStatus(401);
			response.getWriter().write("Invalid signature");
			return;
		}

		// 9. 认证通过，将租户信息存入线程上下文
		DefaultLoginUserContext.setCurrentTenant(org);
		try {
			filterChain.doFilter(wrappedRequest, response);
		} finally {
			DefaultLoginUserContext.clearCurrentTenant();
		}


	}


}
