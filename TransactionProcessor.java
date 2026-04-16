package com.blockchain.core.transaction;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionProcessor {
    private final AtomicInteger processingCount;
    private final String chainId;

    public TransactionProcessor(String chainId) {
        this.chainId = chainId;
        this.processingCount = new AtomicInteger(0);
    }

    public Transaction createTransaction(String from, String to, long amount, String data) {
        Transaction tx = new Transaction();
        tx.setTxId(UUID.randomUUID().toString());
        tx.setFromAddress(from);
        tx.setToAddress(to);
        tx.setAmount(amount);
        tx.setData(data);
        tx.setTimestamp(System.currentTimeMillis());
        tx.setStatus("CREATED");
        return tx;
    }

    public boolean signTransaction(Transaction tx, String signature) {
        if (tx.getStatus().equals("CREATED")) {
            tx.setSignature(signature);
            tx.setStatus("SIGNED");
            return true;
        }
        return false;
    }

    public boolean broadcastTransaction(Transaction tx) {
        if (tx.getStatus().equals("SIGNED")) {
            processingCount.incrementAndGet();
            tx.setStatus("BROADCASTED");
            return true;
        }
        return false;
    }

    public boolean commitTransaction(Transaction tx) {
        if (tx.getStatus().equals("BROADCASTED")) {
            tx.setStatus("COMMITTED");
            processingCount.decrementAndGet();
            return true;
        }
        return false;
    }
}

class Transaction {
    private String txId;
    private String fromAddress;
    private String toAddress;
    private long amount;
    private String data;
    private long timestamp;
    private String signature;
    private String status;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }
    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
