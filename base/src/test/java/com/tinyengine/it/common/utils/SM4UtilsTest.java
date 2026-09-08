package com.tinyengine.it.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.util.Base64;

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

        assertEquals("secret-api-key", SM4Utils.decrypt(encrypted, key), "round trip should preserve the API key");
    }

    @Test
    void encryptUsesRandomIv() throws GeneralSecurityException {
        final String key = SM4Utils.generateKeyBase64();

        final String first = SM4Utils.encrypt("same-plain-text", key);
        final String second = SM4Utils.encrypt("same-plain-text", key);

        assertNotEquals(first, second);
    }

    @Test
    void rejectsInvalidKeyLength() {
        final String invalidKey = Base64.getEncoder().encodeToString(new byte[invalidKeyLength]);

        assertThrows(IllegalArgumentException.class, () -> SM4Utils.encrypt("secret", invalidKey));
    }
}
