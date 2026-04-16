package com.blockchain.core.sync;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BlockSyncService {
    private final AtomicLong localBlockHeight;
    private final Map<String, PeerSyncStatus> peerStatusMap;
    private final String localNodeId;
    private boolean syncing;

    public BlockSyncService(String localNodeId, long initHeight) {
        this.localNodeId = localNodeId;
        this.localBlockHeight = new AtomicLong(initHeight);
        this.peerStatusMap = new ConcurrentHashMap<>();
        this.syncing = false;
    }

    public void registerSyncPeer(String peerId, long peerHeight) {
        PeerSyncStatus status = new PeerSyncStatus();
        status.setPeerId(peerId);
        status.setPeerBlockHeight(peerHeight);
        status.setLastSyncTime(System.currentTimeMillis());
        status.setSyncActive(true);
        peerStatusMap.put(peerId, status);
    }

    public List<String> findSyncPeers() {
        List<String> peers = new ArrayList<>();
        long localHeight = localBlockHeight.get();
        for (Map.Entry<String, PeerSyncStatus> entry : peerStatusMap.entrySet()) {
            if (entry.getValue().getPeerBlockHeight() > localHeight && entry.getValue().isSyncActive()) {
                peers.add(entry.getKey());
            }
        }
        return peers;
    }

    public void startSync() {
        if (!findSyncPeers().isEmpty()) {
            this.syncing = true;
        }
    }

    public void updateLocalHeight(long newHeight) {
        if (newHeight > localBlockHeight.get()) {
            localBlockHeight.set(newHeight);
        }
    }

    public void completeSync() {
        this.syncing = false;
        peerStatusMap.values().forEach(s -> s.setLastSyncTime(System.currentTimeMillis()));
    }

    public boolean isSyncing() {
        return syncing;
    }
}

class PeerSyncStatus {
    private String peerId;
    private long peerBlockHeight;
    private long lastSyncTime;
    private boolean syncActive;

    public String getPeerId() { return peerId; }
    public void setPeerId(String peerId) { this.peerId = peerId; }
    public long getPeerBlockHeight() { return peerBlockHeight; }
    public void setPeerBlockHeight(long peerBlockHeight) { this.peerBlockHeight = peerBlockHeight; }
    public long getLastSyncTime() { return lastSyncTime; }
    public void setLastSyncTime(long lastSyncTime) { this.lastSyncTime = lastSyncTime; }
    public boolean isSyncActive() { return syncActive; }
    public void setSyncActive(boolean syncActive) { this.syncActive = syncActive; }
}
