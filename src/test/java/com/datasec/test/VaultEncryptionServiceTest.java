package com.datasec.test;

import com.datasec.encryption.VaultEncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class VaultEncryptionServiceTest {
    private VaultEncryptionService encryptionService;

    @BeforeEach
    void setUp() throws Exception {
        encryptionService = new VaultEncryptionService();
    }

    @Test
    void testEncryptWithVault() throws Exception {
        String plaintext = "This is a test message";

        String encryptedData = encryptionService.encryptWithVault(plaintext, "encryptionKey");
        String decryptedData = encryptionService.decryptWithVault(encryptedData, "encryptionKey");

        assertNotNull(encryptedData, "Encryption result must not be null");
        assertFalse(encryptedData.isEmpty(), "Encryption result must not be empty");
        assertTrue(encryptedData.length() > plaintext.length(), "Encrypted text must be larger than the original");

        assertNotNull(decryptedData, "Decryption result must not be null");
        assertFalse(decryptedData.isEmpty(), "Decryption result must not be empty");

        assertEquals(decryptedData, plaintext);

    }
}
