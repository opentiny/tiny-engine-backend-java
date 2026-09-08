package com.tinyengine.it.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.util.Base64;

@SuppressWarnings({
    "PMD.AtLeastOneConstructor", "PMD.DataflowAnomalyAnalysis", "PMD.LocalVariableCouldBeFinal"
})
class SM4UtilsTest {
    private static final int SHORT_KEY_LEN = 8;

    @Test
    void encryptAndDecryptRoundTrip() throws GeneralSecurityException {
        String key = SM4Utils.generateKeyBase64();
        String encrypted = SM4Utils.encrypt("secret-api-key", key);

        assertEquals("secret-api-key", SM4Utils.decrypt(encrypted, key), "round trip should preserve the API key");
    }

    @Test
    void encryptUsesRandomIv() throws GeneralSecurityException {
        String key = SM4Utils.generateKeyBase64();

        String first = SM4Utils.encrypt("same-plain-text", key);
        String second = SM4Utils.encrypt("same-plain-text", key);

        assertNotEquals(first, second);
    }

    @Test
    void rejectsInvalidKeyLength() {
        String invalidKey = Base64.getEncoder().encodeToString(new byte[SHORT_KEY_LEN]);

        assertThrows(IllegalArgumentException.class, () -> SM4Utils.encrypt("secret", invalidKey));
    }
}
