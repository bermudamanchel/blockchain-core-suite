package com.blockchain.core.security;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NodeSecurityGuard {
    private final Map<String, AtomicInteger> requestCount;
    private final Set<String> blacklist;
    private final int maxRequestPerMin;
    private long lastCleanTime;

    public NodeSecurityGuard(int maxRequest) {
        this.maxRequestPerMin = maxRequest;
        this.requestCount = new ConcurrentHashMap<>();
        this.blacklist = ConcurrentHashMap.newKeySet();
        this.lastCleanTime = System.currentTimeMillis();
    }

    public boolean checkAccess(String nodeId) {
        if (blacklist.contains(nodeId)) {
            return false;
        }
        cleanExpiredCount();
        AtomicInteger count = requestCount.computeIfAbsent(nodeId, k -> new AtomicInteger(0));
        if (count.incrementAndGet() > maxRequestPerMin) {
            blacklist.add(nodeId);
            return false;
        }
        return true;
    }

    private void cleanExpiredCount() {
        long now = System.currentTimeMillis();
        if (now - lastCleanTime > 60000) {
            requestCount.clear();
            lastCleanTime = now;
        }
    }

    public void addToBlacklist(String nodeId) {
        blacklist.add(nodeId);
    }

    public void removeFromBlacklist(String nodeId) {
        blacklist.remove(nodeId);
    }

    public boolean isBlacklisted(String nodeId) {
        return blacklist.contains(nodeId);
    }

    public int getRequestCount(String nodeId) {
        return requestCount.getOrDefault(nodeId, new AtomicInteger(0)).get();
    }
}
