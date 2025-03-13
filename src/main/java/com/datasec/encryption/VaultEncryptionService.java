package com.datasec.encryption;


import com.datasec.utils.EncryptionUtils;
import com.datasec.utils.FileUtils;
import io.github.jopenlibs.vault.Vault;
import io.github.jopenlibs.vault.VaultConfig;
import io.github.jopenlibs.vault.VaultException;
import io.github.jopenlibs.vault.response.LogicalResponse;
import org.jetbrains.annotations.TestOnly;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class VaultEncryptionService {
    private static final String VAULT_ADDR = System.getenv("VAULT_ADDR");
    private static final String VAULT_SECRET_TOKEN = System.getenv("VAULT_SECRET_TOKEN");

    final Vault vault;

    public VaultEncryptionService() throws VaultException {
        final VaultConfig config = new VaultConfig()
                .address(VAULT_ADDR)
                .token(VAULT_SECRET_TOKEN)
                .engineVersion(1)
                .build();

        vault = Vault.create(config);
    }

    public String encryptWithVault(String plaintext, String filePath) throws VaultException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String key = getEncryptionKeyFromVault(filePath);
        return EncryptionUtils.encryptData(plaintext, key);
    }

    public String decryptWithVault(String cipheredText, String filePath) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, VaultException {
        String key = getDecryptionKeyFromVault(filePath);
        return EncryptionUtils.decryptData(cipheredText, key);
    }

    private String getEncryptionKeyFromVault(String filepath) throws VaultException {
        Map<String, Object> params = new HashMap<>();
        params.put("bits", 256);
        final LogicalResponse writeResponse = vault.logical().write("transit/datakey/plaintext/EncKey1", params);
        String ciphertext = writeResponse.getData().get("ciphertext");
        FileUtils.saveStringToFile(ciphertext, filepath);
        return writeResponse.getData().get("plaintext"); // This will return the key from Vault
    }

    @TestOnly
    private String getDecryptionKeyFromVault(String filePath) throws VaultException {
        Map<String, Object> params = new HashMap<>();
        String encryptedKey = FileUtils.readStringFromFile(filePath).stream().findFirst().orElse(null);
        params.put("ciphertext", encryptedKey);
        final LogicalResponse writeResponse = vault.logical().write("transit/decrypt/EncKey1", params);
        return writeResponse.getData().get("plaintext");
    }
}
