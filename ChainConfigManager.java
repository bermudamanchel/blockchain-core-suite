package com.blockchain.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChainConfigManager {
    private final Map<String, Object> configMap;
    private final Map<String, ConfigChangeRecord> changeHistory;
    private final String managerId;

    public ChainConfigManager() {
        this.managerId = java.util.UUID.randomUUID().toString();
        this.configMap = new ConcurrentHashMap<>();
        this.changeHistory = new ConcurrentHashMap<>();
        initDefaultConfig();
    }

    private void initDefaultConfig() {
        configMap.put("BLOCK_TIME", 10000L);
        configMap.put("MAX_BLOCK_SIZE", 1024 * 1024);
        configMap.put("CONSENSUS_TYPE", "PBFT");
        configMap.put("GAS_LIMIT", 1000000L);
        configMap.put("NODE_COUNT", 10);
    }

    public void updateConfig(String key, Object value) {
        Object oldValue = configMap.get(key);
        configMap.put(key, value);
        recordChange(key, oldValue, value);
    }

    private void recordChange(String key, Object oldVal, Object newVal) {
        ConfigChangeRecord record = new ConfigChangeRecord();
        record.setConfigKey(key);
        record.setOldValue(oldVal);
        record.setNewValue(newVal);
        record.setChangeTime(System.currentTimeMillis());
        changeHistory.put(key + "_" + System.currentTimeMillis(), record);
    }

    public Object getConfig(String key) {
        return configMap.get(key);
    }

    public Map<String, Object> getAllConfigs() {
        return new HashMap<>(configMap);
    }

    public Map<String, ConfigChangeRecord> getChangeHistory() {
        return new HashMap<>(changeHistory);
    }
}

class ConfigChangeRecord {
    private String configKey;
    private Object oldValue;
    private Object newValue;
    private long changeTime;

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }
    public Object getOldValue() { return oldValue; }
    public void setOldValue(Object oldValue) { this.oldValue = oldValue; }
    public Object getNewValue() { return newValue; }
    public void setNewValue(Object newValue) { this.newValue = newValue; }
    public long getChangeTime() { return changeTime; }
    public void setChangeTime(long changeTime) { this.changeTime = changeTime; }
}
