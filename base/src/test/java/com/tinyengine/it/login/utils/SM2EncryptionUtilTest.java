package com.tinyengine.it.login.utils;

import org.bouncycastle.jcajce.provider.util.BadBlockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;


import static org.junit.jupiter.api.Assertions.*;

class SM2EncryptionUtilTest {


	private KeyPair keyPair;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		keyPair = SM2EncryptionUtil.generateSM2KeyPair();
	}

	@Test
	void generateSM2KeyPair() throws Exception {
		assertNotNull(keyPair, "KeyPair should not be null");
		assertNotNull(keyPair.getPrivate(), "PrivateKey should not be null");
		assertNotNull(keyPair.getPublic(), "PublicKey should not be null");
	}

	@Test
	void encryptAndDecrypt() throws Exception {
		String plainText = "Hello, SM2!";
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void getPublicKeyFromBase64() throws Exception {
		PublicKey publicKey = keyPair.getPublic();
		String base64PublicKey = java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());

		PublicKey decodedPublicKey = SM2EncryptionUtil.getPublicKeyFromBase64(base64PublicKey);
		assertNotNull(decodedPublicKey, "Decoded PublicKey should not be null");
		assertEquals(publicKey, decodedPublicKey, "Decoded PublicKey should match the original");
	}

	@Test
	void getPrivateKeyFromBase64() throws Exception {
		PrivateKey privateKey = keyPair.getPrivate();
		String base64PrivateKey = java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded());

		PrivateKey decodedPrivateKey = SM2EncryptionUtil.getPrivateKeyFromBase64(base64PrivateKey);
		assertNotNull(decodedPrivateKey, "Decoded PrivateKey should not be null");
		assertEquals(privateKey, decodedPrivateKey, "Decoded PrivateKey should match the original");
	}

	@Test
	void encryptThrowsExceptionForNullPublicKey() {
		String plainText = "Hello, SM2!";
		assertThrows(java.security.InvalidKeyException.class, () -> SM2EncryptionUtil.encrypt(plainText, null),
			"Encrypting with null PublicKey should throw InvalidKeyException");
	}

	@Test
	void decryptThrowsExceptionForNullPrivateKey() {
		String base64CipherText = "dummyCipherText";
		assertThrows(java.security.InvalidKeyException.class, () -> SM2EncryptionUtil.decrypt(base64CipherText, null),
			"Decrypting with null PrivateKey should throw InvalidKeyException");
	}

	@Test
	void encryptAndDecryptWithEmptyString() {
		String plainText = "";
		PublicKey publicKey = keyPair.getPublic();
		assertThrows(BadBlockException.class, () -> SM2EncryptionUtil.encrypt(plainText, publicKey),
			"Encrypting an empty string should throw BadBlockException");
	}

	@Test
	void encryptAndDecryptWithString() throws Exception {
		String plainText = "hello";
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void encryptAndDecryptWithSpecialCharacters() throws Exception {
		String plainText = "!@#$%^&*()_+{}|:\"<>?";
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void encryptAndDecryptWithLongText() throws Exception {
		String plainText = "a".repeat(1000);
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void getPublicKeyFromInvalidBase64() {
		String invalidBase64 = "invalidBase64Key";
		assertThrows(java.security.spec.InvalidKeySpecException.class, () -> SM2EncryptionUtil.getPublicKeyFromBase64(invalidBase64),
			"Invalid Base64 key should throw InvalidKeySpecException");
	}

	@Test
	void decryptWithInvalidCipherText() {
		String invalidCipherText = "invalidCipherText";
		PrivateKey privateKey = keyPair.getPrivate();

		assertThrows(Exception.class, () -> SM2EncryptionUtil.decrypt(invalidCipherText, privateKey),
			"Decrypting invalid cipher text should throw an exception");
	}

	@Test
	void encryptThrowsExceptionForNullPlainText() {
		PublicKey publicKey = keyPair.getPublic();
		assertThrows(NullPointerException.class, () -> SM2EncryptionUtil.encrypt(null, publicKey),
			"Encrypting with null plain text should throw NullPointerException");
	}

	@Test
	void decryptThrowsExceptionForNullCipherText() {
		PrivateKey privateKey = keyPair.getPrivate();
		assertThrows(NullPointerException.class, () -> SM2EncryptionUtil.decrypt(null, privateKey),
			"Decrypting with null cipher text should throw NullPointerException");
	}

	@Test
	void encryptAndDecryptWithUnicodeCharacters() throws Exception {
		String plainText = "你好，世界！🌍";
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void encryptAndDecryptWithLargeKeyPair() throws Exception {
		KeyPair largeKeyPair = SM2EncryptionUtil.generateSM2KeyPair(); // Assuming larger keys can be generated
		String plainText = "Large key pair test";
		PublicKey publicKey = largeKeyPair.getPublic();
		PrivateKey privateKey = largeKeyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void encryptThrowsExceptionForNullKeyPair() {
		String plainText = "Test with null key pair";
		assertThrows(InvalidKeyException.class, () -> SM2EncryptionUtil.encrypt(plainText, null),
			"Encrypting with null key pair should throw InvalidKeyException");
	}

	@Test
	void decryptThrowsExceptionForTamperedCipherText() throws Exception {
		String plainText = "Tampered cipher text test";
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		String tamperedCipherText = encryptedText.substring(0, encryptedText.length() - 1) + "A";

		assertThrows(Exception.class, () -> SM2EncryptionUtil.decrypt(tamperedCipherText, privateKey),
			"Decrypting tampered cipher text should throw an exception");
	}

	@Test
	void encryptAndDecryptWithMaximumLengthInput() throws Exception {
		String plainText = "a".repeat(245); // Adjust length based on SM2 limits
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void encryptAndDecryptWithNonUTF8Characters() throws Exception {
		String plainText = new String(new byte[]{(byte) 0xC3, (byte) 0x28}, "ISO-8859-1");
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void decryptWithIncorrectPrivateKey() throws Exception {
		String plainText = "Test with incorrect private key";
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);

		KeyPair anotherKeyPair = SM2EncryptionUtil.generateSM2KeyPair();
		PrivateKey incorrectPrivateKey = anotherKeyPair.getPrivate();

		assertThrows(Exception.class, () -> SM2EncryptionUtil.decrypt(encryptedText, incorrectPrivateKey),
			"Decrypting with an incorrect private key should throw an exception");
	}

	@Test
	void encryptAndDecryptWithBinaryData() throws Exception {
		byte[] binaryData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
		String plainText = new String(binaryData, "ISO-8859-1");
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original binary data");
	}

	@Test
	void encryptAndDecryptWithMixedCharacterSets() throws Exception {
		String plainText = "Hello123!@#你好🌟";
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void decryptThrowsExceptionForEmptyCipherText() {
		String emptyCipherText = "";
		PrivateKey privateKey = keyPair.getPrivate();

		assertThrows(BadBlockException.class, () -> SM2EncryptionUtil.decrypt(emptyCipherText, privateKey),
			"Decrypting with empty cipher text should throw BadBlockException");
	}

	@Test
	void encryptAndDecryptWithRepeatedPatterns() throws Exception {
		String plainText = "abc123abc123abc123";
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}

	@Test
	void encryptThrowsExceptionForNullInput() {
		PublicKey publicKey = keyPair.getPublic();
		assertThrows(NullPointerException.class, () -> SM2EncryptionUtil.encrypt(null, publicKey),
			"Encrypting null input should throw NullPointerException");
	}

	@Test
	void decryptThrowsExceptionForCorruptedBase64CipherText() {
		String corruptedCipherText = "corruptedBase64==";
		PrivateKey privateKey = keyPair.getPrivate();

		assertThrows(IllegalArgumentException.class, () -> SM2EncryptionUtil.decrypt(corruptedCipherText, privateKey),
			"Decrypting corrupted Base64 cipher text should throw IllegalArgumentException");
	}

	@Test
	void encryptAndDecryptWithVeryLargeInput() throws Exception {
		String plainText = "a".repeat(5000); // Adjust size based on SM2 limits
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String encryptedText = SM2EncryptionUtil.encrypt(plainText, publicKey);
		assertNotNull(encryptedText, "Encrypted text should not be null");

		String decryptedText = SM2EncryptionUtil.decrypt(encryptedText, privateKey);
		assertNotNull(decryptedText, "Decrypted text should not be null");
		assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
	}
}