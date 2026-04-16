package com.blockchain.core.privacy;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedStorage {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private final Map<String, PrivacyData> storageMap;
    private final String defaultKey;

    public EncryptedStorage(String defaultKey) {
        this.defaultKey = defaultKey;
        this.storageMap = new HashMap<>();
    }

    public String saveEncryptedData(String rawData, String owner) throws Exception {
        String dataId = UUID.randomUUID().toString();
        String encrypted = encrypt(rawData, defaultKey);
        String hash = generateHash(rawData);
        
        PrivacyData data = new PrivacyData();
        data.setDataId(dataId);
        data.setEncryptedData(encrypted);
        data.setDataHash(hash);
        data.setOwner(owner);
        data.setCreateTime(System.currentTimeMillis());
        storageMap.put(dataId, data);
        return dataId;
    }

    public String getDecryptedData(String dataId, String requester) throws Exception {
        PrivacyData data = storageMap.get(dataId);
        if (data == null || !data.getOwner().equals(requester)) {
            return null;
        }
        return decrypt(data.getEncryptedData(), defaultKey);
    }

    public boolean verifyDataIntegrity(String dataId, String rawData) {
        PrivacyData data = storageMap.get(dataId);
        if (data == null) {
            return false;
        }
        return generateHash(rawData).equals(data.getDataHash());
    }

    private String encrypt(String data, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String decrypt(String encrypted, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(decrypted);
    }

    private String generateHash(String data) {
        return Integer.toHexString(data.hashCode());
    }
}

class PrivacyData {
    private String dataId;
    private String encryptedData;
    private String dataHash;
    private String owner;
    private long createTime;

    public String getDataId() { return dataId; }
    public void setDataId(String dataId) { this.dataId = dataId; }
    public String getEncryptedData() { return encryptedData; }
    public void setEncryptedData(String encryptedData) { this.encryptedData = encryptedData; }
    public String getDataHash() { return dataHash; }
    public void setDataHash(String dataHash) { this.dataHash = dataHash; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
}
