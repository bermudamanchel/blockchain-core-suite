package com.blockchain.core.audit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionAudit {
    private final Map<String, AuditRecord> auditRecords;
    private final List<String> riskRules;
    private final String auditId;

    public TransactionAudit() {
        this.auditId = UUID.randomUUID().toString();
        this.auditRecords = new ConcurrentHashMap<>();
        this.riskRules = new ArrayList<>();
        initDefaultRules();
    }

    private void initDefaultRules() {
        riskRules.add("LARGE_AMOUNT");
        riskRules.add("FREQUENT_TX");
        riskRules.add("BLACKLIST_ADDRESS");
    }

    public AuditResult auditTransaction(AuditTx tx) {
        AuditResult result = new AuditResult();
        result.setTxId(tx.getTxId());
        result.setAuditTime(System.currentTimeMillis());
        
        List<String> risks = new ArrayList<>();
        if (tx.getAmount() > 1000000) {
            risks.add("LARGE_AMOUNT");
        }
        if (tx.getTxCount() > 10) {
            risks.add("FREQUENT_TX");
        }
        
        result.setRiskItems(risks);
        result.setPass(risks.isEmpty());
        
        saveAuditRecord(tx, result);
        return result;
    }

    private void saveAuditRecord(AuditTx tx, AuditResult result) {
        AuditRecord record = new AuditRecord();
        record.setTxId(tx.getTxId());
        record.setAmount(tx.getAmount());
        record.setFromAddress(tx.getFromAddress());
        record.setToAddress(tx.getToAddress());
        record.setPass(result.isPass());
        record.setAuditResult(result);
        auditRecords.put(tx.getTxId(), record);
    }

    public List<AuditRecord> getAuditHistory(boolean passOnly) {
        List<AuditRecord> list = new ArrayList<>();
        for (AuditRecord record : auditRecords.values()) {
            if (!passOnly || record.isPass()) {
                list.add(record);
            }
        }
        return list;
    }
}

class AuditTx {
    private String txId;
    private String fromAddress;
    private String toAddress;
    private long amount;
    private int txCount;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }
    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public int getTxCount() { return txCount; }
    public void setTxCount(int txCount) { this.txCount = txCount; }
}

class AuditResult {
    private String txId;
    private boolean pass;
    private List<String> riskItems;
    private long auditTime;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
    public boolean isPass() { return pass; }
    public void setPass(boolean pass) { this.pass = pass; }
    public List<String> getRiskItems() { return riskItems; }
    public void setRiskItems(List<String> riskItems) { this.riskItems = riskItems; }
    public long getAuditTime() { return auditTime; }
    public void setAuditTime(long auditTime) { this.auditTime = auditTime; }
}

class AuditRecord {
    private String txId;
    private String fromAddress;
    private String toAddress;
    private long amount;
    private boolean pass;
    private AuditResult auditResult;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }
    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public boolean isPass() { return pass; }
    public void setPass(boolean pass) { this.pass = pass; }
    public AuditResult getAuditResult() { return auditResult; }
    public void setAuditResult(AuditResult auditResult) { this.auditResult = auditResult; }
}
