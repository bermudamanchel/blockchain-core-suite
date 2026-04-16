package com.blockchain.core.discovery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PeerDiscovery {
    private final String localNodeId;
    private final Set<String> bootNodes;
    private final Map<String, PeerInfo> discoveredPeers;
    private final List<String> activePeers;

    public PeerDiscovery(String localNodeId, List<String> bootNodes) {
        this.localNodeId = localNodeId;
        this.bootNodes = new CopyOnWriteArraySet<>(bootNodes);
        this.discoveredPeers = new ConcurrentHashMap<>();
        this.activePeers = new CopyOnWriteArrayList<>();
    }

    public void startDiscovery() {
        for (String boot : bootNodes) {
            discoverFromNode(boot);
        }
    }

    private void discoverFromNode(String nodeId) {
        if (!discoveredPeers.containsKey(nodeId) && !nodeId.equals(localNodeId)) {
            PeerInfo info = new PeerInfo();
            info.setNodeId(nodeId);
            info.setDiscoveredTime(System.currentTimeMillis());
            info.setStatus("DISCOVERED");
            discoveredPeers.put(nodeId, info);
        }
    }

    public List<String> getNewPeers() {
        List<String> newPeers = new ArrayList<>();
        for (Map.Entry<String, PeerInfo> entry : discoveredPeers.entrySet()) {
            if (entry.getValue().getStatus().equals("DISCOVERED")) {
                newPeers.add(entry.getKey());
                entry.getValue().setStatus("VERIFIED");
            }
        }
        return newPeers;
    }

    public void addActivePeer(String nodeId) {
        if (!activePeers.contains(nodeId) && discoveredPeers.containsKey(nodeId)) {
            activePeers.add(nodeId);
            discoveredPeers.get(nodeId).setStatus("ACTIVE");
        }
    }

    public List<String> getAllDiscoveredPeers() {
        return new ArrayList<>(discoveredPeers.keySet());
    }
}

class PeerInfo {
    private String nodeId;
    private long discoveredTime;
    private String status;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public long getDiscoveredTime() { return discoveredTime; }
    public void setDiscoveredTime(long discoveredTime) { this.discoveredTime = discoveredTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
