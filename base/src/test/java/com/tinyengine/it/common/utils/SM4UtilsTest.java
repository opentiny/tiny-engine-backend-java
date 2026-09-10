package com.tinyengine.it.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.LawOfDemeter"
})
class SM4UtilsTest {
    private static final int SHORT_KEY_LEN = 8;
    private final int invalidKeyLength;

    /* default */ SM4UtilsTest() {
        invalidKeyLength = SHORT_KEY_LEN;
    }

    @Test
    void encryptAndDecryptRoundTrip() throws GeneralSecurityException {
        final String key = SM4Utils.generateKeyBase64();
        final String encrypted = SM4Utils.encrypt("secret-api-key", key);

        assertEquals(
                "secret-api-key",
                SM4Utils.decrypt(encrypted, key),
                "round trip should preserve the API key");
    }

    @Test
    void encryptUsesVersionedGcmPayload() throws GeneralSecurityException {
        final String key = SM4Utils.generateKeyBase64();
        final String encrypted = SM4Utils.encrypt("secret-api-key", key);

        assertTrue(encrypted.startsWith("GCM1:"), "new payloads must identify the GCM format");
    }

    @Test
    void decryptsLegacyEcbPayload() throws GeneralSecurityException {
        final String key = SM4Utils.generateKeyBase64();
        final byte[] keyBytes = Base64.getDecoder().decode(key);
        final Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "SM4"));
        final String legacyPayload =
                Base64.getEncoder()
                        .encodeToString(
                                cipher.doFinal(
                                        "legacy-api-key".getBytes(StandardCharsets.UTF_8)));

        assertEquals(
                "legacy-api-key",
                SM4Utils.decrypt(legacyPayload, key),
                "legacy ECB payloads must remain decryptable during migration");
    }

    @Test
    void decryptsUnversionedGcmPayload() throws GeneralSecurityException {
        final String key = SM4Utils.generateKeyBase64();
        final String versionedPayload = SM4Utils.encrypt("unversioned-gcm", key);
        final String rawGcmPayload = versionedPayload.substring("GCM1:".length());

        assertEquals(
                "unversioned-gcm",
                SM4Utils.decrypt(rawGcmPayload, key),
                "unversioned GCM payloads from the previous release must remain decryptable");
    }

    @Test
    void encryptUsesRandomIv() throws GeneralSecurityException {
        final String key = SM4Utils.generateKeyBase64();

        final String first = SM4Utils.encrypt("same-plain-text", key);
        final String second = SM4Utils.encrypt("same-plain-text", key);

        assertNotEquals(first, second, "GCM encryption must use a fresh nonce");
    }

    @Test
    void rejectsInvalidKeyLength() {
        final String invalidKey = Base64.getEncoder().encodeToString(new byte[invalidKeyLength]);

        assertThrows(
                IllegalArgumentException.class,
                () -> SM4Utils.encrypt("secret", invalidKey),
                "invalid SM4 keys must be rejected");
    }
}
