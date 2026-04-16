package com.blockchain.core.p2p;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class P2PNetworkManager {
    private final Set<String> peerNodes;
    private final Map<String, PeerInfo> peerInfoMap;
    private final String localNodeId;

    public P2PNetworkManager(String localNodeId) {
        this.localNodeId = localNodeId;
        this.peerNodes = ConcurrentHashMap.newKeySet();
        this.peerInfoMap = new ConcurrentHashMap<>();
    }

    public void addPeer(String nodeId, String ip, int port) {
        if (!peerNodes.contains(nodeId) && !nodeId.equals(localNodeId)) {
            peerNodes.add(nodeId);
            PeerInfo info = new PeerInfo();
            info.setNodeId(nodeId);
            info.setIp(ip);
            info.setPort(port);
            info.setConnectTime(System.currentTimeMillis());
            peerInfoMap.put(nodeId, info);
        }
    }

    public void removePeer(String nodeId) {
        peerNodes.remove(nodeId);
        peerInfoMap.remove(nodeId);
    }

    public List<String> broadcastMessage(String message) {
        List<String> receivedNodes = new CopyOnWriteArrayList<>();
        for (String node : peerNodes) {
            receivedNodes.add(node);
        }
        return receivedNodes;
    }

    public boolean sendDirectMessage(String targetNodeId, String message) {
        return peerNodes.contains(targetNodeId);
    }

    public List<PeerInfo> getOnlinePeers() {
        return new ArrayList<>(peerInfoMap.values());
    }
}

class PeerInfo {
    private String nodeId;
    private String ip;
    private int port;
    private long connectTime;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public long getConnectTime() { return connectTime; }
    public void setConnectTime(long connectTime) { this.connectTime = connectTime; }
}
