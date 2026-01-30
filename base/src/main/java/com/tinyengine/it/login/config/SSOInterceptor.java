/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.login.config;

import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.login.utils.JwtUtil;
import com.tinyengine.it.login.config.context.DefaultLoginUserContext;
import com.tinyengine.it.login.model.UserInfo;
import com.tinyengine.it.mapper.AuthUsersUnitsRolesMapper;
import com.tinyengine.it.model.entity.Tenant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * SSO Interceptor
 */
@Slf4j
@Component
public class SSOInterceptor implements HandlerInterceptor {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	AuthUsersUnitsRolesMapper authUsersUnitsRolesMapper;

	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response, Object handler) throws Exception {

		String authorization = request.getHeader("Authorization");
		String org = request.getHeader("X-Lowcode-Org");
		// 如果没有token，重定向到登录页
		if (authorization == null || authorization.isEmpty()) {
			log.info("No token");
			throw new ServiceException(ExceptionEnum.CM336.getResultCode(), ExceptionEnum.CM336.getResultMsg());
		}
		String token = jwtUtil.getTokenFromRequest(authorization);
		String requestURI = request.getRequestURI();

		log.info("Intercepting: {}, Token: {}", requestURI, token != null ? "present" : "null");

		try {
			// 验证token
			if (!jwtUtil.validateToken(token)) {
				log.warn("Token validation failed");
				throw new ServiceException(ExceptionEnum.CM339.getResultCode(), ExceptionEnum.CM339.getResultMsg());
			}

			// 从token中获取用户信息
			String username = jwtUtil.getUsernameFromToken(token);
			String userId = jwtUtil.getUserIdFromToken(token);
			String roles = jwtUtil.getRolesFromToken(token);
			Integer platformId = jwtUtil.getPlatformIdFromToken(token);


			// 检查必需的用户信息
			if (username == null || username.isEmpty() || userId == null) {
				log.warn("User information is incomplete - username: {}, userId: {}", username, userId);
				throw new ServiceException(ExceptionEnum.CM339.getResultCode(), ExceptionEnum.CM339.getResultMsg());
			}
			int userIdInt;
			try {
				userIdInt = Integer.parseInt(userId);
			} catch (NumberFormatException e) {
				log.error("Invalid userId format: {}", userId);
				throw new ServiceException(ExceptionEnum.CM342.getResultCode(), ExceptionEnum.CM342.getResultMsg());
			}
			List<Tenant> tenants = authUsersUnitsRolesMapper.queryAllTenantByUserId(userIdInt);
			if (tenants == null) {
				log.warn("No tenants found for userId: {}", userId);
				throw new ServiceException(ExceptionEnum.CM340.getResultCode(), ExceptionEnum.CM340.getResultMsg());
			}

			if (!"null".equals(org) && org != null) {
				boolean findOrg = false;
				for (Tenant tenant : tenants) {
					tenant.setIsInUse(tenant.getId().equals(org));
					if (tenant.getIsInUse()) {
						findOrg = true;
					}
				}
				if (!findOrg) {
					log.warn("X-Lowcode-Org not found in user's tenants - X-Lowcode-Org: {}", org);
					throw new ServiceException(ExceptionEnum.CM341.getResultCode(), ExceptionEnum.CM341.getResultMsg());
				}
			}
			// 存储用户信息到LoginUserContext
			UserInfo userInfo = new UserInfo(userId, username, tenants);

			userInfo.setPlatformId(platformId != null ? platformId : 0);
			userInfo.setRoles(roles != null ? roles : "USER");
			userInfo.setToken(token);

			DefaultLoginUserContext.setCurrentUser(userInfo);

			log.info("Token validated and user context set for user: {}", username);
			return true;

		} catch (Exception e) {
			log.error("Token validation exception: {}", e.getMessage(), e);
			DefaultLoginUserContext.clear();
			throw new ServiceException(ExceptionEnum.CM336.getResultCode(), ExceptionEnum.CM336.getResultMsg());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
	                            HttpServletResponse response, Object handler, Exception ex) {
		// 请求完成后清理用户上下文
		DefaultLoginUserContext.clear();

		log.debug("Cleared user context for request completion");
	}
}
