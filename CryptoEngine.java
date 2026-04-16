package com.blockchain.core.crypto;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptoEngine {
    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String SM4_ALGORITHM = "SM4/ECB/PKCS5Padding";

    public String generateSHA256Hash(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);
        byte[] hash = digest.digest(data.getBytes());
        return bytesToHex(hash);
    }

    public String encryptSM4(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "SM4");
        Cipher cipher = Cipher.getInstance(SM4_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decryptSM4(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "SM4");
        Cipher cipher = Cipher.getInstance(SM4_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted);
    }

    public byte[] signData(byte[] data, PrivateKey privateKey) throws Exception {
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        java.security.Signature sig = java.security.Signature.getInstance("SHA256withECDSA");
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
