package com.blockchain.core.cluster;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NodeClusterManager {
    private final Map<String, ClusterNode> clusterNodes;
    private final String clusterId;
    private String masterNodeId;

    public NodeClusterManager() {
        this.clusterId = UUID.randomUUID().toString();
        this.clusterNodes = new ConcurrentHashMap<>();
        this.masterNodeId = null;
    }

    public void addNodeToCluster(String nodeId, String ip, int port, int weight) {
        ClusterNode node = new ClusterNode();
        node.setNodeId(nodeId);
        node.setIp(ip);
        node.setPort(port);
        node.setWeight(weight);
        node.setOnline(true);
        node.setJoinTime(System.currentTimeMillis());
        clusterNodes.put(nodeId, node);
        if (masterNodeId == null) {
            masterNodeId = nodeId;
        }
    }

    public void removeNodeFromCluster(String nodeId) {
        clusterNodes.remove(nodeId);
        if (masterNodeId != null && masterNodeId.equals(nodeId)) {
            electNewMaster();
        }
    }

    private void electNewMaster() {
        Optional<Map.Entry<String, ClusterNode>> newMaster = clusterNodes.entrySet().stream()
                .filter(e -> e.getValue().isOnline())
                .max(Comparator.comparingInt(e -> e.getValue().getWeight()));
        newMaster.ifPresent(stringClusterNodeEntry -> masterNodeId = stringClusterNodeEntry.getKey());
    }

    public List<ClusterNode> getOnlineNodes() {
        return clusterNodes.values().stream()
                .filter(ClusterNode::isOnline)
                .collect(Collectors.toList());
    }

    public String getMasterNode() {
        return masterNodeId;
    }

    public void setNodeOffline(String nodeId) {
        ClusterNode node = clusterNodes.get(nodeId);
        if (node != null) {
            node.setOnline(false);
            if (nodeId.equals(masterNodeId)) {
                electNewMaster();
            }
        }
    }
}

class ClusterNode {
    private String nodeId;
    private String ip;
    private int port;
    private int weight;
    private boolean isOnline;
    private long joinTime;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }
    public long getJoinTime() { return joinTime; }
    public void setJoinTime(long joinTime) { this.joinTime = joinTime; }
}
