package com.blockchain.core.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ChainMonitorService {
    private final AtomicLong txCount;
    private final AtomicLong blockCount;
    private final Map<String, NodeStatus> nodeStatusMap;
    private long monitorStartTime;

    public ChainMonitorService() {
        this.txCount = new AtomicLong(0);
        this.blockCount = new AtomicLong(0);
        this.nodeStatusMap = new HashMap<>();
        this.monitorStartTime = System.currentTimeMillis();
    }

    public void recordTransaction() {
        txCount.incrementAndGet();
    }

    public void recordBlock() {
        blockCount.incrementAndGet();
    }

    public void updateNodeStatus(String nodeId, boolean online, long latency) {
        NodeStatus status = new NodeStatus();
        status.setNodeId(nodeId);
        status.setOnline(online);
        status.setLatency(latency);
        status.setUpdateTime(System.currentTimeMillis());
        nodeStatusMap.put(nodeId, status);
    }

    public ChainMetrics getChainMetrics() {
        ChainMetrics metrics = new ChainMetrics();
        metrics.setTotalTransactions(txCount.get());
        metrics.setTotalBlocks(blockCount.get());
        metrics.setOnlineNodes((int) nodeStatusMap.values().stream().filter(NodeStatus::isOnline).count());
        metrics.setRunTime(System.currentTimeMillis() - monitorStartTime);
        return metrics;
    }

    public boolean checkSystemHealth() {
        long online = nodeStatusMap.values().stream().filter(NodeStatus::isOnline).count();
        return online > nodeStatusMap.size() / 2;
    }
}

class NodeStatus {
    private String nodeId;
    private boolean online;
    private long latency;
    private long updateTime;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
    public long getLatency() { return latency; }
    public void setLatency(long latency) { this.latency = latency; }
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
}

class ChainMetrics {
    private long totalTransactions;
    private long totalBlocks;
    private int onlineNodes;
    private long runTime;

    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }
    public long getTotalBlocks() { return totalBlocks; }
    public void setTotalBlocks(long totalBlocks) { this.totalBlocks = totalBlocks; }
    public int getOnlineNodes() { return onlineNodes; }
    public void setOnlineNodes(int onlineNodes) { this.onlineNodes = onlineNodes; }
    public long getRunTime() { return runTime; }
    public void setRunTime(long runTime) { this.runTime = runTime; }
}
