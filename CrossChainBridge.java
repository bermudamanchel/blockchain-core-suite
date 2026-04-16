package com.blockchain.core.crosschain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrossChainBridge {
    private final Map<String, CrossChainTask> taskMap;
    private final String bridgeAddress;
    private final Map<String, String> chainMapping;

    public CrossChainBridge(String bridgeAddress) {
        this.bridgeAddress = bridgeAddress;
        this.taskMap = new HashMap<>();
        this.chainMapping = new HashMap<>();
    }

    public void registerChain(String chainName, String chainRpc) {
        chainMapping.put(chainName, chainRpc);
    }

    public String createCrossChainTask(String sourceChain, String targetChain, String asset, long amount) {
        if (!chainMapping.containsKey(sourceChain) || !chainMapping.containsKey(targetChain)) {
            throw new IllegalArgumentException("Chain not registered");
        }
        String taskId = UUID.randomUUID().toString();
        CrossChainTask task = new CrossChainTask();
        task.setTaskId(taskId);
        task.setSourceChain(sourceChain);
        task.setTargetChain(targetChain);
        task.setAsset(asset);
        task.setAmount(amount);
        task.setStatus("INIT");
        task.setCreateTime(System.currentTimeMillis());
        taskMap.put(taskId, task);
        return taskId;
    }

    public boolean executeCrossChain(String taskId) {
        CrossChainTask task = taskMap.get(taskId);
        if (task != null && task.getStatus().equals("INIT")) {
            task.setStatus("PROCESSING");
            task.setExecuteTime(System.currentTimeMillis());
            return true;
        }
        return false;
    }

    public boolean completeCrossChain(String taskId) {
        CrossChainTask task = taskMap.get(taskId);
        if (task != null && task.getStatus().equals("PROCESSING")) {
            task.setStatus("COMPLETED");
            task.setCompleteTime(System.currentTimeMillis());
            return true;
        }
        return false;
    }
}

class CrossChainTask {
    private String taskId;
    private String sourceChain;
    private String targetChain;
    private String asset;
    private long amount;
    private String status;
    private long createTime;
    private long executeTime;
    private long completeTime;

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getSourceChain() { return sourceChain; }
    public void setSourceChain(String sourceChain) { this.sourceChain = sourceChain; }
    public String getTargetChain() { return targetChain; }
    public void setTargetChain(String targetChain) { this.targetChain = targetChain; }
    public String getAsset() { return asset; }
    public void setAsset(String asset) { this.asset = asset; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getExecuteTime() { return executeTime; }
    public void setExecuteTime(long executeTime) { this.executeTime = executeTime; }
    public long getCompleteTime() { return completeTime; }
    public void setCompleteTime(long completeTime) { this.completeTime = completeTime; }
}
