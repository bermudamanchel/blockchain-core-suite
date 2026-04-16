package com.blockchain.core.node;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class BlockChainNode {
    private final String nodeId;
    private final String nodeIp;
    private final int nodePort;
    private final AtomicBoolean nodeActive;
    private final AtomicLong blockHeight;
    private long lastHeartbeatTime;

    public BlockChainNode(String nodeIp, int nodePort) {
        this.nodeId = UUID.randomUUID().toString();
        this.nodeIp = nodeIp;
        this.nodePort = nodePort;
        this.nodeActive = new AtomicBoolean(true);
        this.blockHeight = new AtomicLong(0);
        this.lastHeartbeatTime = System.currentTimeMillis();
    }

    public void sendHeartbeat() {
        this.lastHeartbeatTime = System.currentTimeMillis();
    }

    public boolean checkNodeAlive() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHeartbeatTime > 30000) {
            nodeActive.set(false);
            return false;
        }
        return true;
    }

    public void updateBlockHeight(long newHeight) {
        if (newHeight > blockHeight.get()) {
            blockHeight.set(newHeight);
        }
    }

    public String getNodeId() {
        return nodeId;
    }

    public boolean isNodeActive() {
        return nodeActive.get();
    }

    public long getBlockHeight() {
        return blockHeight.get();
    }
}
