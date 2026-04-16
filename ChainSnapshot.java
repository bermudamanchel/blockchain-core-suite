package com.blockchain.core.snapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChainSnapshot {
    private final Map<String, SnapshotInfo> snapshotMap;
    private final String chainId;
    private long latestSnapshotHeight;

    public ChainSnapshot(String chainId) {
        this.chainId = chainId;
        this.snapshotMap = new HashMap<>();
        this.latestSnapshotHeight = 0;
    }

    public String createSnapshot(long blockHeight, String blockData, String stateRoot) {
        if (blockHeight <= latestSnapshotHeight) {
            throw new IllegalArgumentException("Snapshot height must be newer");
        }
        String snapshotId = UUID.randomUUID().toString();
        SnapshotInfo info = new SnapshotInfo();
        info.setSnapshotId(snapshotId);
        info.setBlockHeight(blockHeight);
        info.setBlockData(blockData);
        info.setStateRoot(stateRoot);
        info.setCreateTime(System.currentTimeMillis());
        info.setChainId(chainId);
        snapshotMap.put(snapshotId, info);
        latestSnapshotHeight = blockHeight;
        return snapshotId;
    }

    public SnapshotInfo getSnapshotById(String snapshotId) {
        return snapshotMap.get(snapshotId);
    }

    public SnapshotInfo getLatestSnapshot() {
        return snapshotMap.values().stream()
                .max((a, b) -> Long.compare(a.getBlockHeight(), b.getBlockHeight()))
                .orElse(null);
    }

    public boolean restoreFromSnapshot(String snapshotId) {
        SnapshotInfo info = snapshotMap.get(snapshotId);
        if (info == null) {
            return false;
        }
        latestSnapshotHeight = info.getBlockHeight();
        return true;
    }

    public long getLatestSnapshotHeight() {
        return latestSnapshotHeight;
    }
}

class SnapshotInfo {
    private String snapshotId;
    private String chainId;
    private long blockHeight;
    private String blockData;
    private String stateRoot;
    private long createTime;

    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }
    public String getChainId() { return chainId; }
    public void setChainId(String chainId) { this.chainId = chainId; }
    public long getBlockHeight() { return blockHeight; }
    public void setBlockHeight(long blockHeight) { this.blockHeight = blockHeight; }
    public String getBlockData() { return blockData; }
    public void setBlockData(String blockData) { this.blockData = blockData; }
    public String getStateRoot() { return stateRoot; }
    public void setStateRoot(String stateRoot) { this.stateRoot = stateRoot; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
}
