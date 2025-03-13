package com.datasec.utils;

import org.jetbrains.annotations.TestOnly;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {

    private EncryptionUtils() {
    }

    public static String encryptData(String plaintext, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = getSecretKey(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        byte[] encryptedData = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(ciphertext, 0, encryptedData, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    @TestOnly
    public static String decryptData(String encryptedData, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] iv = new byte[16];
        byte[] ciphertext = new byte[decodedData.length - iv.length];

        System.arraycopy(decodedData, 0, iv, 0, iv.length);
        System.arraycopy(decodedData, iv.length, ciphertext, 0, ciphertext.length);

        SecretKey secretKey = getSecretKey(key);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(ciphertext);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    private static SecretKey getSecretKey(String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        return new SecretKeySpec(keyBytes, "AES");
    }
}
