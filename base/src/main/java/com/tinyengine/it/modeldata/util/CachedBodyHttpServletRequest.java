package com.tinyengine.it.modeldata.util;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
	private final byte[] cachedBody;

	public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
	}


	@Override
	public ServletInputStream getInputStream() {
		return new CachedBodyServletInputStream(this.cachedBody);
	}
	@Override
	public BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(
			new ByteArrayInputStream(this.cachedBody), StandardCharsets.UTF_8
		));
	}

	public String getBodyString() {
		return new String(this.cachedBody, StandardCharsets.UTF_8);
	}
}
