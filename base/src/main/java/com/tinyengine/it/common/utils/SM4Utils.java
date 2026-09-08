package com.tinyengine.it.common.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("PMD.LawOfDemeter")
public final class SM4Utils {

    private static final String ALGORITHM = "SM4";
    private static final String TRANSFORMATION = "SM4/GCM/NoPadding";
    private static final int KEY_SIZE = 128;
    private static final int KEY_LENGTH_BYTES = KEY_SIZE / Byte.SIZE;
    private static final int IV_LENGTH_BYTES = 12;
    private static final int GCM_TAG_BITS = 128;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private SM4Utils() {
        // Utility class.
    }

    /**
     * 生成 SM4 密钥.
     *
     * @return generated SM4 key encoded as Base64
     */
    public static String generateKeyBase64() throws GeneralSecurityException {
        final byte[] key = generateKey();
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(key);
    }

    public static byte[] generateKey() throws GeneralSecurityException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM, "BC");
        keyGenerator.init(KEY_SIZE, SECURE_RANDOM);
        final SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static String encrypt(final String apiKey, final String base64Key)
            throws GeneralSecurityException {
        final byte[] key = decodeKey(base64Key);
        final byte[] nonce = new byte[IV_LENGTH_BYTES];
        SECURE_RANDOM.nextBytes(nonce);

        final byte[] encrypted =
                doCipher(
                        Cipher.ENCRYPT_MODE,
                        apiKey.getBytes(StandardCharsets.UTF_8),
                        key,
                        nonce);
        final ByteBuffer outputBuffer =
                ByteBuffer.allocate(nonce.length + encrypted.length);
        outputBuffer.put(nonce);
        outputBuffer.put(encrypted);
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(outputBuffer.array());
    }

    public static String decrypt(final String encryptedBase64, final String base64Key)
            throws GeneralSecurityException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final byte[] encryptedWithIv = decoder.decode(encryptedBase64);
        if (encryptedWithIv.length <= IV_LENGTH_BYTES) {
            throw new IllegalArgumentException("Invalid encrypted payload");
        }

        final byte[] key = decodeKey(base64Key);
        final ByteBuffer buffer = ByteBuffer.wrap(encryptedWithIv);
        final byte[] nonce = new byte[IV_LENGTH_BYTES];
        buffer.get(nonce);
        final byte[] encrypted = new byte[buffer.remaining()];
        buffer.get(encrypted);

        final byte[] decrypted =
                doCipher(Cipher.DECRYPT_MODE, encrypted, key, nonce);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static byte[] doCipher(
            final int mode,
            final byte[] data,
            final byte[] key,
            final byte[] nonce)
            throws GeneralSecurityException {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        final GCMParameterSpec parameterSpec =
                new GCMParameterSpec(GCM_TAG_BITS, nonce);
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION, "BC");
        cipher.init(mode, secretKeySpec, parameterSpec);
        return cipher.doFinal(data);
    }

    private static byte[] decodeKey(final String base64Key) {
        final Base64.Decoder decoder = Base64.getDecoder();
        final byte[] key = decoder.decode(base64Key);
        if (key.length != KEY_LENGTH_BYTES) {
            throw new IllegalArgumentException("SM4 key must be 128 bits");
        }
        return key;
    }
}
