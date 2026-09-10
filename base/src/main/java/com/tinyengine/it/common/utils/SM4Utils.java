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
@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.LawOfDemeter"
})
public final class SM4Utils {

    private static final String ALGORITHM = "SM4";
    private static final String TRANSFORMATION = "SM4/GCM/NoPadding";
    private static final String LEGACY_TRANSFORM = "SM4/ECB/PKCS5Padding";
    private static final String GCM_PREFIX = "GCM1:";
    private static final int KEY_SIZE = 128;
    private static final int KEY_LENGTH_BYTES = KEY_SIZE / Byte.SIZE;
    private static final int IV_LENGTH_BYTES = 12;
    private static final int GCM_TAG_BITS = 128;
    private static final Base64Codec BASE64_CODEC = new Base64Codec();
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
        return BASE64_CODEC.encode(generateKey());
    }

    public static byte[] generateKey() throws GeneralSecurityException {
        return new KeyGeneratorService().generate();
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
        final PayloadBuffer outputBuffer = new PayloadBuffer(nonce.length + encrypted.length);
        outputBuffer.append(nonce);
        outputBuffer.append(encrypted);
        return GCM_PREFIX + BASE64_CODEC.encode(outputBuffer.toByteArray());
    }

    public static String decrypt(final String encryptedBase64, final String base64Key)
            throws GeneralSecurityException {
        final byte[] key = decodeKey(base64Key);
        String decrypted;
        if (encryptedBase64.startsWith(GCM_PREFIX)) {
            decrypted = decryptGcm(encryptedBase64.substring(GCM_PREFIX.length()), key);
        } else {
            try {
                // Keep compatibility with GCM payloads created before the version prefix was added.
                decrypted = decryptGcm(encryptedBase64, key);
            } catch (GeneralSecurityException | IllegalArgumentException exception) {
                decrypted = decryptLegacyEcb(encryptedBase64, key);
            }
        }
        return decrypted;
    }

    private static String decryptGcm(final String encryptedBase64, final byte[] key)
            throws GeneralSecurityException {
        final byte[] encryptedWithIv = BASE64_CODEC.decode(encryptedBase64);
        if (encryptedWithIv.length <= IV_LENGTH_BYTES) {
            throw new IllegalArgumentException("Invalid encrypted payload");
        }

        final PayloadBuffer buffer = new PayloadBuffer(encryptedWithIv);
        final byte[] nonce = new byte[IV_LENGTH_BYTES];
        buffer.read(nonce);
        final byte[] encrypted = new byte[buffer.remaining()];
        buffer.read(encrypted);

        final byte[] decrypted =
                doCipher(Cipher.DECRYPT_MODE, encrypted, key, nonce);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private static String decryptLegacyEcb(final String encryptedBase64, final byte[] key)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(LEGACY_TRANSFORM, "BC");
        final SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        final byte[] decrypted = cipher.doFinal(BASE64_CODEC.decode(encryptedBase64));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static byte[] doCipher(
            final int mode,
            final byte[] data,
            final byte[] key,
            final byte[] nonce)
            throws GeneralSecurityException {
        return new CipherService().process(mode, data, key, nonce);
    }

    private static byte[] decodeKey(final String base64Key) {
        final byte[] key = BASE64_CODEC.decode(base64Key);
        if (key.length != KEY_LENGTH_BYTES) {
            throw new IllegalArgumentException("SM4 key must be 128 bits");
        }
        return key;
    }

    private static final class Base64Codec {
        private final Base64.Encoder encoder;
        private final Base64.Decoder decoder;

        private Base64Codec() {
            encoder = Base64.getEncoder();
            decoder = Base64.getDecoder();
        }

        private String encode(final byte[] value) {
            return encoder.encodeToString(value);
        }

        private byte[] decode(final String value) {
            return decoder.decode(value);
        }
    }

    private static final class KeyGeneratorService {
        private final KeyGenerator generator;

        private KeyGeneratorService() throws GeneralSecurityException {
            generator = KeyGenerator.getInstance(ALGORITHM, "BC");
            generator.init(KEY_SIZE, SECURE_RANDOM);
        }

        private byte[] generate() {
            return getEncodedKey(generator.generateKey());
        }

        private byte[] getEncodedKey(final SecretKey key) {
            return key.getEncoded();
        }
    }

    private static final class CipherService {
        private final Cipher cipher;

        private CipherService() throws GeneralSecurityException {
            cipher = Cipher.getInstance(TRANSFORMATION, "BC");
        }

        private byte[] process(
                final int mode,
                final byte[] data,
                final byte[] key,
                final byte[] nonce)
                throws GeneralSecurityException {
            final SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            final GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_BITS, nonce);
            cipher.init(mode, secretKey, parameterSpec);
            return cipher.doFinal(data);
        }
    }

    private static final class PayloadBuffer {
        private final ByteBuffer buffer;

        private PayloadBuffer(final int capacity) {
            buffer = ByteBuffer.allocate(capacity);
        }

        private PayloadBuffer(final byte[] content) {
            buffer = ByteBuffer.wrap(content);
        }

        private void append(final byte[] content) {
            buffer.put(content);
        }

        private void read(final byte[] target) {
            buffer.get(target);
        }

        private int remaining() {
            return buffer.remaining();
        }

        private byte[] toByteArray() {
            return buffer.array();
        }
    }
}
