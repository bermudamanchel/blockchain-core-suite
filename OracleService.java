package com.blockchain.core.oracle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OracleService {
    private final String oracleId;
    private final Map<String, DataSource> dataSources;
    private final Map<String, OracleResult> resultMap;
    private boolean serviceEnabled;

    public OracleService() {
        this.oracleId = UUID.randomUUID().toString();
        this.dataSources = new ConcurrentHashMap<>();
        this.resultMap = new ConcurrentHashMap<>();
        this.serviceEnabled = true;
    }

    public void registerDataSource(String sourceId, String sourceUrl, String sourceType) {
        DataSource source = new DataSource();
        source.setSourceId(sourceId);
        source.setSourceUrl(sourceUrl);
        source.setSourceType(sourceType);
        source.setEnabled(true);
        dataSources.put(sourceId, source);
    }

    public String requestData(String requestId, String sourceId, Map<String, Object> params) {
        if (!serviceEnabled || !dataSources.containsKey(sourceId)) {
            return null;
        }
        OracleResult result = new OracleResult();
        result.setRequestId(requestId);
        result.setSourceId(sourceId);
        result.setRequestTime(System.currentTimeMillis());
        result.setStatus("PENDING");
        resultMap.put(requestId, result);
        return requestId;
    }

    public boolean fulfillData(String requestId, String data) {
        OracleResult result = resultMap.get(requestId);
        if (result == null || !result.getStatus().equals("PENDING")) {
            return false;
        }
        result.setData(data);
        result.setStatus("FULFILLED");
        result.setFulfillTime(System.currentTimeMillis());
        return true;
    }

    public void startService() {
        this.serviceEnabled = true;
    }

    public void stopService() {
        this.serviceEnabled = false;
    }
}

class DataSource {
    private String sourceId;
    private String sourceUrl;
    private String sourceType;
    private boolean enabled;

    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

class OracleResult {
    private String requestId;
    private String sourceId;
    private String data;
    private String status;
    private long requestTime;
    private long fulfillTime;

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getRequestTime() { return requestTime; }
    public void setRequestTime(long requestTime) { this.requestTime = requestTime; }
    public long getFulfillTime() { return fulfillTime; }
    public void setFulfillTime(long fulfillTime) { this.fulfillTime = fulfillTime; }
}
