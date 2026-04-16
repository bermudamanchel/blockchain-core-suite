package com.blockchain.core.analytics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChainAnalytics {
    private final Map<Long, Long> dailyTxCount;
    private final Map<String, Long> nodeActivity;
    private final Map<String, Long> assetFlow;
    private long totalTx;

    public ChainAnalytics() {
        this.dailyTxCount = new ConcurrentHashMap<>();
        this.nodeActivity = new ConcurrentHashMap<>();
        this.assetFlow = new ConcurrentHashMap<>();
        this.totalTx = 0;
    }

    public void recordTransaction(long timestamp, String nodeId, long assetAmount) {
        long day = timestamp / (24 * 60 * 60 * 1000);
        dailyTxCount.put(day, dailyTxCount.getOrDefault(day, 0L) + 1);
        nodeActivity.put(nodeId, nodeActivity.getOrDefault(nodeId, 0L) + 1);
        assetFlow.put(nodeId, assetFlow.getOrDefault(nodeId, 0L) + assetAmount);
        totalTx++;
    }

    public long getDailyTransactions(long day) {
        return dailyTxCount.getOrDefault(day, 0L);
    }

    public List<Map.Entry<String, Long>> getTopActiveNodes(int limit) {
        return nodeActivity.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getAssetDistribution() {
        return new HashMap<>(assetFlow);
    }

    public ChainReport generateReport() {
        ChainReport report = new ChainReport();
        report.setTotalTransactions(totalTx);
        report.setActiveNodes(nodeActivity.size());
        report.setLatestDayTx(dailyTxCount.getOrDefault(dailyTxCount.keySet().stream().max(Long::compareTo).orElse(0L), 0L));
        return report;
    }
}

class ChainReport {
    private long totalTransactions;
    private int activeNodes;
    private long latestDayTx;

    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }
    public int getActiveNodes() { return activeNodes; }
    public void setActiveNodes(int activeNodes) { this.activeNodes = activeNodes; }
    public long getLatestDayTx() { return latestDayTx; }
    public void setLatestDayTx(long latestDayTx) { this.latestDayTx = latestDayTx; }
}
