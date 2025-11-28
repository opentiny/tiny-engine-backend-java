/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.login.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * SM2工具类
 */
public class SM2EncryptionUtil {
    static {
        // 注册Bouncy Castle Provider
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * SM2获取密钥对
     */
    public static KeyPair generateSM2KeyPair() throws Exception {
        // 获取ECC算法的KeyPairGenerator实例
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);

        // 使用国密SM2推荐的椭圆曲线参数：prime256v1 或 sm2p256v1
        // 在Bouncy Castle中，通常使用 "sm2p256v1"
        ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");

        // 初始化密钥对生成器
        keyPairGenerator.initialize(sm2Spec, new SecureRandom());

        // 生成并返回密钥对
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * SM2加密
     */
    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        // 获取SM2加密的Cipher实例，使用 "SM2" 算法
        Cipher cipher = Cipher.getInstance("SM2", BouncyCastleProvider.PROVIDER_NAME);

        // 初始化为加密模式，传入公钥
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 执行加密
        byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));

        // 将二进制密文转换为Base64字符串，便于传输和显示
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * SM2解密
     */
    public static String decrypt(String base64CipherText, PrivateKey privateKey) throws Exception {
        // 获取SM2解密的Cipher实例
        Cipher cipher = Cipher.getInstance("SM2", BouncyCastleProvider.PROVIDER_NAME);

        // 初始化为解密模式，传入私钥
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 先将Base64字符串解码为二进制
        byte[] cipherText = Base64.getDecoder().decode(base64CipherText);

        // 执行解密
        byte[] decryptedText = cipher.doFinal(cipherText);

        // 将解密后的字节数组转换为字符串
        return new String(decryptedText, "UTF-8");
    }

    /**
     * base64PublicKey 解码
     */
    public static PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
        // Base64 解码
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);

        // 创建密钥规范
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // 获取密钥工厂（根据算法选择）
        KeyFactory keyFactory = KeyFactory.getInstance("EC"); // 对于 SM2/ECC

        // 生成公钥
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * base64PrivateKey 解码
     */
    public static PrivateKey getPrivateKeyFromBase64(String base64PrivateKey) throws Exception {
        // Base64 解码
        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);

        // 创建密钥规范
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance("EC"); // 对于 SM2/ECC

        // 生成私钥
        return keyFactory.generatePrivate(keySpec);
    }
}
