package com.blockchain.core.wallet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WalletCore {
    private final String walletId;
    private final Map<String, String> addressKeyMap;
    private final Map<String, Long> assetBalanceMap;
    private final List<WalletTransaction> txHistory;

    public WalletCore() {
        this.walletId = UUID.randomUUID().toString();
        this.addressKeyMap = new ConcurrentHashMap<>();
        this.assetBalanceMap = new ConcurrentHashMap<>();
        this.txHistory = new ArrayList<>();
    }

    public String createNewAddress() {
        String address = "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 40);
        String privateKey = UUID.randomUUID().toString().replace("-", "");
        addressKeyMap.put(address, privateKey);
        assetBalanceMap.put(address, 0L);
        return address;
    }

    public long getBalance(String address) {
        return assetBalanceMap.getOrDefault(address, 0L);
    }

    public boolean transfer(String from, String to, long amount) {
        if (addressKeyMap.containsKey(from) && assetBalanceMap.get(from) >= amount) {
            assetBalanceMap.put(from, assetBalanceMap.get(from) - amount);
            assetBalanceMap.put(to, assetBalanceMap.getOrDefault(to, 0L) + amount);
            recordTransaction(from, to, amount);
            return true;
        }
        return false;
    }

    private void recordTransaction(String from, String to, long amount) {
        WalletTransaction tx = new WalletTransaction();
        tx.setTxId(UUID.randomUUID().toString());
        tx.setFrom(from);
        tx.setTo(to);
        tx.setAmount(amount);
        tx.setTimestamp(System.currentTimeMillis());
        txHistory.add(tx);
    }

    public List<WalletTransaction> getTransactionHistory() {
        return new ArrayList<>(txHistory);
    }
}

class WalletTransaction {
    private String txId;
    private String from;
    private String to;
    private long amount;
    private long timestamp;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
