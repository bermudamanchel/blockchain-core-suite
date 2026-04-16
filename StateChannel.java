package com.blockchain.core.channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StateChannel {
    private final String channelId;
    private final String partyA;
    private final String partyB;
    private final Map<Long, ChannelState> stateHistory;
    private boolean channelOpen;
    private long latestSequence;

    public StateChannel(String partyA, String partyB) {
        this.channelId = UUID.randomUUID().toString();
        this.partyA = partyA;
        this.partyB = partyB;
        this.stateHistory = new ConcurrentHashMap<>();
        this.channelOpen = true;
        this.latestSequence = 0;
    }

    public ChannelState createState(long balanceA, long balanceB) {
        if (!channelOpen) {
            return null;
        }
        latestSequence++;
        ChannelState state = new ChannelState();
        state.setSequence(latestSequence);
        state.setBalanceA(balanceA);
        state.setBalanceB(balanceB);
        state.setTimestamp(System.currentTimeMillis());
        state.setChannelId(channelId);
        stateHistory.put(latestSequence, state);
        return state;
    }

    public boolean signState(String party, long sequence, String signature) {
        ChannelState state = stateHistory.get(sequence);
        if (state == null || !(party.equals(partyA) || party.equals(partyB))) {
            return false;
        }
        if (party.equals(partyA)) {
            state.setSignatureA(signature);
        } else {
            state.setSignatureB(signature);
        }
        return true;
    }

    public boolean closeChannel(long finalSequence) {
        ChannelState finalState = stateHistory.get(finalSequence);
        if (finalState != null && finalState.getSignatureA() != null && finalState.getSignatureB() != null) {
            this.channelOpen = false;
            return true;
        }
        return false;
    }

    public ChannelState getLatestState() {
        return stateHistory.get(latestSequence);
    }

    public boolean isChannelOpen() {
        return channelOpen;
    }
}

class ChannelState {
    private String channelId;
    private long sequence;
    private long balanceA;
    private long balanceB;
    private String signatureA;
    private String signatureB;
    private long timestamp;

    public long getSequence() { return sequence; }
    public void setSequence(long sequence) { this.sequence = sequence; }
    public long getBalanceA() { return balanceA; }
    public void setBalanceA(long balanceA) { this.balanceA = balanceA; }
    public long getBalanceB() { return balanceB; }
    public void setBalanceB(long balanceB) { this.balanceB = balanceB; }
    public String getSignatureA() { return signatureA; }
    public void setSignatureA(String signatureA) { this.signatureA = signatureA; }
    public String getSignatureB() { return signatureB; }
    public void setSignatureB(String signatureB) { this.signatureB = signatureB; }
    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
