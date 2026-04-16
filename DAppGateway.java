package com.blockchain.core.gateway;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DAppGateway {
    private final Map<String, DAppInfo> registeredApps;
    private final Map<String, String> sessionTokenMap;
    private final String gatewayId;

    public DAppGateway() {
        this.gatewayId = UUID.randomUUID().toString();
        this.registeredApps = new ConcurrentHashMap<>();
        this.sessionTokenMap = new ConcurrentHashMap<>();
    }

    public String registerDApp(String appName, String appUrl) {
        String appId = UUID.randomUUID().toString();
        DAppInfo info = new DAppInfo();
        info.setAppId(appId);
        info.setAppName(appName);
        info.setAppUrl(appUrl);
        info.setRegisterTime(System.currentTimeMillis());
        info.setEnabled(true);
        registeredApps.put(appId, info);
        return appId;
    }

    public String createSession(String appId) {
        if (registeredApps.containsKey(appId) && registeredApps.get(appId).isEnabled()) {
            String token = UUID.randomUUID().toString();
            sessionTokenMap.put(token, appId);
            return token;
        }
        return null;
    }

    public Map<String, Object> requestChainData(String token, String method, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        if (sessionTokenMap.containsKey(token)) {
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", handleRequest(method, params));
        } else {
            result.put("code", 401);
            result.put("msg", "invalid token");
        }
        return result;
    }

    private Object handleRequest(String method, Map<String, Object> params) {
        return params != null ? new HashMap<>(params) : new HashMap<>();
    }
}

class DAppInfo {
    private String appId;
    private String appName;
    private String appUrl;
    private long registerTime;
    private boolean enabled;

    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getAppUrl() { return appUrl; }
    public void setAppUrl(String appUrl) { this.appUrl = appUrl; }
    public long getRegisterTime() { return registerTime; }
    public void setRegisterTime(long registerTime) { this.registerTime = registerTime; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
