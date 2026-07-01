package com.tinyengine.it.modeldata.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
	private static final int GCM_TAG_LENGTH = 128;      // 认证标签长度（位）
	private static final int GCM_IV_LENGTH = 12;        // 推荐 12 字节（96 位）
	private static final int AES_KEY_SIZE = 256;        // 密钥长度

	private AESUtil() {
		// 工具类私有构造
	}

	/**
	 * 加密数据
	 *
	 * @param plainText 明文（UTF-8 字符串）
	 * @param keyBase64 Base64 编码的密钥（256 位）
	 * @return Base64 编码的密文（格式：IV + 密文 + 认证标签）
	 * @throws Exception 加密失败
	 */
	public static String encrypt(String plainText, String keyBase64) throws Exception {
		if (plainText == null || keyBase64 == null) {
			throw new IllegalArgumentException("明文和密钥不能为空");
		}

		byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
		SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

		// 生成随机 IV
		byte[] iv = new byte[GCM_IV_LENGTH];
		SecureRandom secureRandom = SecureRandom.getInstanceStrong();
		secureRandom.nextBytes(iv);

		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

		byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

		// 合并 IV + 密文
		byte[] combined = new byte[iv.length + cipherText.length];
		System.arraycopy(iv, 0, combined, 0, iv.length);
		System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

		return Base64.getEncoder().encodeToString(combined);
	}

	/**
	 * 解密数据
	 *
	 * @param encryptedBase64 Base64 编码的密文（IV + 密文 + 认证标签）
	 * @param keyBase64       Base64 编码的密钥（必须与加密时一致）
	 * @return 明文字符串
	 * @throws Exception 解密失败（数据被篡改、密钥错误等）
	 */
	public static String decrypt(String encryptedBase64, String keyBase64) throws Exception {
		if (encryptedBase64 == null || keyBase64 == null) {
			throw new IllegalArgumentException("密文和密钥不能为空");
		}

		byte[] combined = Base64.getDecoder().decode(encryptedBase64);
		if (combined.length < GCM_IV_LENGTH) {
			throw new IllegalArgumentException("密文数据长度不足");
		}

		// 分离 IV 和密文
		byte[] iv = new byte[GCM_IV_LENGTH];
		byte[] cipherText = new byte[combined.length - GCM_IV_LENGTH];
		System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
		System.arraycopy(combined, GCM_IV_LENGTH, cipherText, 0, cipherText.length);

		byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
		SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

		byte[] plainBytes = cipher.doFinal(cipherText);
		return new String(plainBytes, StandardCharsets.UTF_8);
	}

	/**
	 * 生成一个 Base64 编码的 AES-256 密钥（用于初始化）
	 */
	public static String generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
		keyGen.init(AES_KEY_SIZE, SecureRandom.getInstanceStrong());
		SecretKey secretKey = keyGen.generateKey();
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	// 测试入口
	public static void main(String[] args) throws Exception {
		// 生成密钥（实际使用时从环境变量读取）
		String key = generateKey();
		System.out.println("AES Key (Base64): " + key);

		// 加密
		String plain = "sk_123456";
		String encrypted = encrypt(plain, key);
		System.out.println("加密后: " + encrypted);

		// 解密
		String decrypted = decrypt(encrypted, key);
		System.out.println("解密后: " + decrypted);

		// 篡改测试（会抛出异常）
		// System.out.println(decrypt(encrypted.substring(0, encrypted.length()-2), key));
	}
}
