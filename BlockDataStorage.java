package com.blockchain.core.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BlockDataStorage {
    private final Map<String, String> blockStorage;
    private final Map<Long, String> heightToHashMap;
    private final String storageType;

    public BlockDataStorage(String storageType) {
        this.blockStorage = new HashMap<>();
        this.heightToHashMap = new HashMap<>();
        this.storageType = storageType;
    }

    public void saveBlock(String blockHash, long blockHeight, String blockData) {
        blockStorage.put(blockHash, blockData);
        heightToHashMap.put(blockHeight, blockHash);
    }

    public Optional<String> getBlockByHash(String blockHash) {
        return Optional.ofNullable(blockStorage.get(blockHash));
    }

    public Optional<String> getBlockByHeight(long blockHeight) {
        String hash = heightToHashMap.get(blockHeight);
        return Optional.ofNullable(hash != null ? blockStorage.get(hash) : null);
    }

    public boolean deleteBlock(long blockHeight) {
        String hash = heightToHashMap.remove(blockHeight);
        if (hash != null) {
            blockStorage.remove(hash);
            return true;
        }
        return false;
    }

    public long getLatestBlockHeight() {
        return heightToHashMap.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
    }
}
