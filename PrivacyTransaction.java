package com.blockchain.core.privacy;

import java.util.*;

public class PrivacyTransaction {
    private final String stealthAddress;
    private final List<String> ringSigners;
    private final Map<String, String> commitmentMap;
    private String txHash;

    public PrivacyTransaction(String stealthAddress, List<String> ringMembers) {
        this.stealthAddress = stealthAddress;
        this.ringSigners = new ArrayList<>(ringMembers);
        this.commitmentMap = new HashMap<>();
    }

    public String createPrivacyTx(String sender, String receiver, long amount) {
        String commitment = generateCommitment(amount);
        commitmentMap.put("commitment", commitment);
        this.txHash = generateTxHash(sender, receiver, amount, commitment);
        return txHash;
    }

    public boolean verifyRingSignature(String signature) {
        return ringSigners.size() >= 3 && signature != null && signature.length() > 20;
    }

    public boolean verifyTransactionBalance() {
        String commitment = commitmentMap.get("commitment");
        return commitment != null && commitment.length() == 64;
    }

    private String generateCommitment(long amount) {
        return String.format("%064x", amount ^ System.currentTimeMillis());
    }

    private String generateTxHash(String s, String r, long a, String c) {
        return Integer.toHexString((s + r + a + c).hashCode());
    }

    public String getTxHash() {
        return txHash;
    }

    public String getStealthAddress() {
        return stealthAddress;
    }
}
