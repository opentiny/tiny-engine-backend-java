package com.tinyengine.it.mcp.utils;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.config.OpenAIConfig;
import org.springframework.stereotype.Component;

import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
@Component
public class UrlValidateUtil {
	private static final Set<String> LOOPBACK_HOSTS = Set.of("localhost", "127.0.0.1", "::1", "[::1]");
	private final OpenAIConfig config;

	public UrlValidateUtil(OpenAIConfig config) {
		this.config = config;
	}


	public void validateHost(String host) {

		if (host == null || host.isEmpty()) {
			throw new ServiceException("400", "Invalid baseUrl: missing host");
		}

		boolean isLoopback = LOOPBACK_HOSTS.contains(host.toLowerCase());

		List<String> allowedHosts = config.getAllowedHosts();

		if (allowedHosts == null || allowedHosts.isEmpty()) {
			if (!config.isAllowAnyHost()) {
				throw new ServiceException("500", "No AI allowed hosts configured");
			}

			return;
		}

		boolean matched = allowedHosts.stream()
			.anyMatch(allowed -> allowed.equalsIgnoreCase(host));
		if (!matched) {
			throw new ServiceException("400",
				"Host not allowed: " + host + ". Allowed hosts: " + allowedHosts);
		}

		if (isLoopback) {
			throw new ServiceException("400", "Loopback addresses are not allowed for custom baseUrl");
		}

	}




}
