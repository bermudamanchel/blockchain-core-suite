package com.blockchain.core.zkp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ZeroKnowledgeProof {
    private final Map<String, ProofData> proofMap;
    private final int securityLevel;

    public ZeroKnowledgeProof(int securityLevel) {
        this.securityLevel = securityLevel;
        this.proofMap = new HashMap<>();
    }

    public String generateProof(String secret, String publicData) {
        String proofId = UUID.randomUUID().toString();
        ProofData proof = new ProofData();
        proof.setProofId(proofId);
        proof.setSecretHash(generateHash(secret));
        proof.setPublicData(publicData);
        proof.setProofValue(generateProofValue(secret, publicData));
        proof.setCreateTime(System.currentTimeMillis());
        proofMap.put(proofId, proof);
        return proofId;
    }

    public boolean verifyProof(String proofId, String publicData) {
        ProofData proof = proofMap.get(proofId);
        if (proof == null || !proof.getPublicData().equals(publicData)) {
            return false;
        }
        return validateProofValue(proof.getProofValue(), proof.getSecretHash(), publicData);
    }

    private String generateHash(String data) {
        return Integer.toHexString(data.hashCode() ^ securityLevel);
    }

    private String generateProofValue(String secret, String publicData) {
        return generateHash(secret + publicData + securityLevel);
    }

    private boolean validateProofValue(String proof, String secretHash, String publicData) {
        String expected = generateHash(secretHash + publicData + securityLevel);
        return proof.equals(expected);
    }
}

class ProofData {
    private String proofId;
    private String secretHash;
    private String publicData;
    private String proofValue;
    private long createTime;

    public String getProofId() { return proofId; }
    public void setProofId(String proofId) { this.proofId = proofId; }
    public String getSecretHash() { return secretHash; }
    public void setSecretHash(String secretHash) { this.secretHash = secretHash; }
    public String getPublicData() { return publicData; }
    public void setPublicData(String publicData) { this.publicData = publicData; }
    public String getProofValue() { return proofValue; }
    public void setProofValue(String proofValue) { this.proofValue = proofValue; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
}
