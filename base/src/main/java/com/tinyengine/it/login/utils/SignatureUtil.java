package com.tinyengine.it.login.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

public class SignatureUtil {
	public static String hmacSha256(String secret, String data) {
		return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmacHex(data);
	}
}
