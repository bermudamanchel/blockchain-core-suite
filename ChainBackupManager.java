package com.blockchain.core.backup;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChainBackupManager {
    private final Map<String, BackupTask> backupTasks;
    private final List<String> backupLocations;
    private final String managerId;
    private boolean autoBackupEnabled;

    public ChainBackupManager() {
        this.managerId = UUID.randomUUID().toString();
        this.backupTasks = new ConcurrentHashMap<>();
        this.backupLocations = new ArrayList<>();
        this.autoBackupEnabled = false;
    }

    public void addBackupLocation(String location) {
        if (!backupLocations.contains(location)) {
            backupLocations.add(location);
        }
    }

    public String createBackupTask(long blockHeight, String location) {
        if (!backupLocations.contains(location)) {
            return null;
        }
        String taskId = UUID.randomUUID().toString();
        BackupTask task = new BackupTask();
        task.setTaskId(taskId);
        task.setBlockHeight(blockHeight);
        task.setBackupLocation(location);
        task.setStatus("CREATED");
        task.setCreateTime(System.currentTimeMillis());
        backupTasks.put(taskId, task);
        return taskId;
    }

    public boolean executeBackup(String taskId) {
        BackupTask task = backupTasks.get(taskId);
        if (task == null || !task.getStatus().equals("CREATED")) {
            return false;
        }
        task.setStatus("BACKING_UP");
        task.setStartTime(System.currentTimeMillis());
        return true;
    }

    public boolean completeBackup(String taskId) {
        BackupTask task = backupTasks.get(taskId);
        if (task == null || !task.getStatus().equals("BACKING_UP")) {
            return false;
        }
        task.setStatus("COMPLETED");
        task.setFinishTime(System.currentTimeMillis());
        return true;
    }

    public void setAutoBackup(boolean enable) {
        this.autoBackupEnabled = enable;
    }
}

class BackupTask {
    private String taskId;
    private long blockHeight;
    private String backupLocation;
    private String status;
    private long createTime;
    private long startTime;
    private long finishTime;

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public long getBlockHeight() { return blockHeight; }
    public void setBlockHeight(long blockHeight) { this.blockHeight = blockHeight; }
    public String getBackupLocation() { return backupLocation; }
    public void setBackupLocation(String backupLocation) { this.backupLocation = backupLocation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getFinishTime() { return finishTime; }
    public void setFinishTime(long finishTime) { this.finishTime = finishTime; }
}
