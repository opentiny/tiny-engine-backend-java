package com.tinyengine.it.modeldata.util;

public class CachedBodyServletInputStream extends jakarta.servlet.ServletInputStream {
	private final java.io.ByteArrayInputStream inputStream;

	public CachedBodyServletInputStream(byte[] cachedBody) {
		this.inputStream = new java.io.ByteArrayInputStream(cachedBody);
	}

	@Override
	public int read() {
		return inputStream.read();
	}

	@Override
	public boolean isFinished() {
		return inputStream.available() == 0;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(jakarta.servlet.ReadListener readListener) {
		throw new UnsupportedOperationException();
	}
}
