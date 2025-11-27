package com.tinyengine.it.common.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class SM4Utils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String ALGORITHM = "SM4";
    private static final String TRANSFORMATION_ECB = "SM4/ECB/PKCS5Padding";
    private static final int KEY_SIZE = 128;

    /**
     * 生成 SM4 密钥
     */
    public static String generateKeyBase64() throws Exception {
        byte[] key = generateKey();
        return Base64.getEncoder().encodeToString(key);
    }

    public static byte[] generateKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM, "BC");
        kg.init(KEY_SIZE, new SecureRandom());
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * ECB 模式加密 - 只加密API密钥值 (Base64 结果)
     */
    public static String encryptECB(String apiKey, String base64Key) throws Exception {
        byte[] key = Base64.getDecoder().decode(base64Key);
        byte[] encrypted = encryptECB(apiKey.getBytes("UTF-8"), key);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * ECB 模式解密 - 直接返回API密钥
     */
    public static String decryptECB(String encryptedBase64, String base64Key) throws Exception {
        byte[] key = Base64.getDecoder().decode(base64Key);
        byte[] encrypted = Base64.getDecoder().decode(encryptedBase64);
        byte[] decrypted = decryptECB(encrypted, key);
        return new String(decrypted, "UTF-8");
    }

    // ECB 模式的底层方法保持不变
    private static byte[] encryptECB(byte[] data, byte[] key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_ECB, "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(data);
    }

    private static byte[] decryptECB(byte[] encryptedData, byte[] key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_ECB, "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return cipher.doFinal(encryptedData);
    }

}
