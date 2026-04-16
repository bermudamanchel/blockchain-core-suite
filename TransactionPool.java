package com.blockchain.core.pool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TransactionPool {
    private final Map<String, PoolTransaction> pendingTransactions;
    private final List<String> priorityTransactions;
    private final int maxPoolSize;

    public TransactionPool(int maxSize) {
        this.maxPoolSize = maxSize;
        this.pendingTransactions = new ConcurrentHashMap<>();
        this.priorityTransactions = new CopyOnWriteArrayList<>();
    }

    public boolean addTransaction(PoolTransaction tx) {
        if (pendingTransactions.size() >= maxPoolSize) {
            return false;
        }
        if (pendingTransactions.containsKey(tx.getTxId())) {
            return false;
        }
        pendingTransactions.put(tx.getTxId(), tx);
        if (tx.getPriority() > 5) {
            priorityTransactions.add(tx.getTxId());
        }
        return true;
    }

    public boolean removeTransaction(String txId) {
        priorityTransactions.remove(txId);
        return pendingTransactions.remove(txId) != null;
    }

    public List<PoolTransaction> getBatchTransactions(int batchSize) {
        List<PoolTransaction> batch = new ArrayList<>();
        for (String txId : priorityTransactions) {
            if (batch.size() < batchSize && pendingTransactions.containsKey(txId)) {
                batch.add(pendingTransactions.get(txId));
            }
        }
        for (PoolTransaction tx : pendingTransactions.values()) {
            if (batch.size() >= batchSize) break;
            if (!priorityTransactions.contains(tx.getTxId())) {
                batch.add(tx);
            }
        }
        return batch;
    }

    public int getPoolSize() {
        return pendingTransactions.size();
    }

    public List<PoolTransaction> getAllPendingTransactions() {
        return new ArrayList<>(pendingTransactions.values());
    }
}

class PoolTransaction {
    private String txId;
    private long gasPrice;
    private int priority;
    private long timestamp;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
    public long getGasPrice() { return gasPrice; }
    public void setGasPrice(long gasPrice) { this.gasPrice = gasPrice; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
