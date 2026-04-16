package com.blockchain.core.consensus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockConsensus {
    private final List<String> consensusNodes;
    private final Map<String, Integer> nodeVotes;
    private final int requiredVotes;
    private boolean consensusReached;

    public BlockConsensus(List<String> nodeList) {
        this.consensusNodes = new CopyOnWriteArrayList<>(nodeList);
        this.nodeVotes = new ConcurrentHashMap<>();
        this.requiredVotes = (consensusNodes.size() * 2) / 3 + 1;
        this.consensusReached = false;
    }

    public void castVote(String nodeId, String blockHash) {
        if (consensusNodes.contains(nodeId) && !nodeVotes.containsKey(nodeId)) {
            nodeVotes.put(nodeId, 1);
            checkConsensusStatus();
        }
    }

    private void checkConsensusStatus() {
        int totalVotes = nodeVotes.values().stream().mapToInt(Integer::intValue).sum();
        if (totalVotes >= requiredVotes) {
            this.consensusReached = true;
        }
    }

    public boolean isConsensusReached() {
        return consensusReached;
    }

    public void resetConsensus() {
        nodeVotes.clear();
        consensusReached = false;
    }
}
