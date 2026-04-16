package com.blockchain.core.event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContractEventBus {
    private final Map<String, List<ContractEventListener>> listenerMap;
    private final List<ContractEvent> eventHistory;
    private final String busId;

    public ContractEventBus() {
        this.busId = UUID.randomUUID().toString();
        this.listenerMap = new ConcurrentHashMap<>();
        this.eventHistory = new CopyOnWriteArrayList<>();
    }

    public void registerListener(String eventType, ContractEventListener listener) {
        listenerMap.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public void unregisterListener(String eventType, ContractEventListener listener) {
        List<ContractEventListener> listeners = listenerMap.get(eventType);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public void publishEvent(ContractEvent event) {
        eventHistory.add(event);
        List<ContractEventListener> listeners = listenerMap.get(event.getEventType());
        if (listeners != null) {
            for (ContractEventListener listener : listeners) {
                listener.onEvent(event);
            }
        }
    }

    public List<ContractEvent> getEventHistory(String eventType) {
        List<ContractEvent> result = new ArrayList<>();
        for (ContractEvent event : eventHistory) {
            if (event.getEventType().equals(eventType)) {
                result.add(event);
            }
        }
        return result;
    }

    public int getEventCount() {
        return eventHistory.size();
    }
}

interface ContractEventListener {
    void onEvent(ContractEvent event);
}

class ContractEvent {
    private String eventId;
    private String eventType;
    private String contractAddress;
    private Map<String, Object> data;
    private long timestamp;

    public ContractEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getContractAddress() { return contractAddress; }
    public void setContractAddress(String contractAddress) { this.contractAddress = contractAddress; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}
