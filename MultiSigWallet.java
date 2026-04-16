package com.blockchain.core.wallet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MultiSigWallet {
    private final String walletId;
    private final List<String> ownerList;
    private final int requiredSignatures;
    private final Map<String, MultiSigTransaction> txMap;
    private final Map<String, Long> balanceMap;

    public MultiSigWallet(List<String> owners, int required) {
        this.walletId = UUID.randomUUID().toString();
        this.ownerList = new ArrayList<>(owners);
        this.requiredSignatures = required;
        this.txMap = new ConcurrentHashMap<>();
        this.balanceMap = new ConcurrentHashMap<>();
    }

    public String createTransaction(String creator, String to, long amount) {
        if (!ownerList.contains(creator)) {
            throw new SecurityException("Not wallet owner");
        }
        String txId = UUID.randomUUID().toString();
        MultiSigTransaction tx = new MultiSigTransaction();
        tx.setTxId(txId);
        tx.setToAddress(to);
        tx.setAmount(amount);
        tx.setCreator(creator);
        tx.setStatus("CREATED");
        tx.setSigners(new HashSet<>());
        txMap.put(txId, tx);
        return txId;
    }

    public boolean signTransaction(String signer, String txId) {
        MultiSigTransaction tx = txMap.get(txId);
        if (tx == null || !ownerList.contains(signer) || tx.getSigners().contains(signer)) {
            return false;
        }
        tx.getSigners().add(signer);
        if (tx.getSigners().size() >= requiredSignatures) {
            tx.setStatus("READY");
        }
        return true;
    }

    public boolean executeTransaction(String txId) {
        MultiSigTransaction tx = txMap.get(txId);
        if (tx == null || !tx.getStatus().equals("READY")) {
            return false;
        }
        long balance = balanceMap.getOrDefault(this.walletId, 0L);
        if (balance >= tx.getAmount()) {
            balanceMap.put(this.walletId, balance - tx.getAmount());
            tx.setStatus("EXECUTED");
            return true;
        }
        return false;
    }

    public void deposit(long amount) {
        balanceMap.put(walletId, balanceMap.getOrDefault(walletId, 0L) + amount);
    }
}

class MultiSigTransaction {
    private String txId;
    private String toAddress;
    private long amount;
    private String creator;
    private String status;
    private Set<String> signers;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Set<String> getSigners() { return signers; }
    public void setSigners(Set<String> signers) { this.signers = signers; }
}
