package com.blockchain.core.sharding;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataShardingUtil {
    private final int shardCount;
    private final Map<Integer, List<String>> shardMap;
    private final Map<String, Integer> dataShardMapping;

    public DataShardingUtil(int shardCount) {
        this.shardCount = shardCount;
        this.shardMap = new ConcurrentHashMap<>();
        this.dataShardMapping = new ConcurrentHashMap<>();
        for (int i = 0; i < shardCount; i++) {
            shardMap.put(i, new ArrayList<>());
        }
    }

    public int assignDataToShard(String dataKey) {
        int shardId = Math.abs(dataKey.hashCode()) % shardCount;
        dataShardMapping.put(dataKey, shardId);
        shardMap.get(shardId).add(dataKey);
        return shardId;
    }

    public List<String> getDataByShard(int shardId) {
        if (shardId < 0 || shardId >= shardCount) {
            return Collections.emptyList();
        }
        return new ArrayList<>(shardMap.get(shardId));
    }

    public int getDataShard(String dataKey) {
        return dataShardMapping.getOrDefault(dataKey, -1);
    }

    public boolean removeData(String dataKey) {
        Integer shardId = dataShardMapping.remove(dataKey);
        if (shardId != null) {
            shardMap.get(shardId).remove(dataKey);
            return true;
        }
        return false;
    }

    public Map<Integer, Integer> getShardDataCount() {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : shardMap.entrySet()) {
            countMap.put(entry.getKey(), entry.getValue().size());
        }
        return countMap;
    }
}
