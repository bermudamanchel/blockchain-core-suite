package com.blockchain.core.validate;

public class BlockValidator {
    private static final int DIFFICULTY = 4;
    private final String previousBlockHash;

    public BlockValidator(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public boolean validateBlockHash(String blockHash) {
        return blockHash.startsWith("0".repeat(DIFFICULTY));
    }

    public boolean validatePreviousHash(String currentPrevHash) {
        return previousBlockHash.equals(currentPrevHash);
    }

    public boolean validateTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        return timestamp > 0 && timestamp <= currentTime;
    }

    public boolean validateBlockData(String blockData) {
        return blockData != null && !blockData.isEmpty() && blockData.length() < 1024 * 1024;
    }

    public boolean fullBlockValidation(Block block) {
        return validateBlockHash(block.getBlockHash())
                && validatePreviousHash(block.getPreviousHash())
                && validateTimestamp(block.getTimestamp())
                && validateBlockData(block.getBlockData());
    }
}

class Block {
    private String blockHash;
    private String previousHash;
    private long timestamp;
    private String blockData;

    public String getBlockHash() { return blockHash; }
    public void setBlockHash(String blockHash) { this.blockHash = blockHash; }
    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getBlockData() { return blockData; }
    public void setBlockData(String blockData) { this.blockData = blockData; }
}
