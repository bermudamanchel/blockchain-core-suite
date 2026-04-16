package com.blockchain.core.merkle;

import java.security.MessageDigest;
import java.util.*;

public class MerkleTreeUtil {
    private final List<String> transactions;
    private List<String> merkleNodes;
    private String rootHash;

    public MerkleTreeUtil(List<String> txList) {
        this.transactions = new ArrayList<>(txList);
        this.merkleNodes = new ArrayList<>();
        generateMerkleTree();
    }

    private void generateMerkleTree() {
        if (transactions.isEmpty()) {
            rootHash = "";
            return;
        }
        List<String> levelNodes = new ArrayList<>();
        for (String tx : transactions) {
            levelNodes.add(sha256(tx));
        }
        merkleNodes.addAll(levelNodes);
        while (levelNodes.size() > 1) {
            levelNodes = generateNextLevel(levelNodes);
            merkleNodes.addAll(levelNodes);
        }
        rootHash = levelNodes.get(0);
    }

    private List<String> generateNextLevel(List<String> currentLevel) {
        List<String> nextLevel = new ArrayList<>();
        int i = 0;
        while (i < currentLevel.size()) {
            String left = currentLevel.get(i);
            String right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left;
            nextLevel.add(sha256(left + right));
            i += 2;
        }
        return nextLevel;
    }

    private String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            return "";
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String getMerkleRoot() {
        return rootHash;
    }

    public boolean verifyTransaction(String tx) {
        String txHash = sha256(tx);
        return merkleNodes.contains(txHash);
    }
}
