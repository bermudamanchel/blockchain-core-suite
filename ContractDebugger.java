package com.blockchain.core.debug;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ContractDebugger {
    private final Map<String, DebugSession> sessionMap;
    private final List<String> breakpoints;
    private boolean debugEnabled;

    public ContractDebugger() {
        this.sessionMap = new ConcurrentHashMap<>();
        this.breakpoints = new ArrayList<>();
        this.debugEnabled = true;
    }

    public String startDebugSession(String contractAddress, String contractCode) {
        String sessionId = UUID.randomUUID().toString();
        DebugSession session = new DebugSession();
        session.setSessionId(sessionId);
        session.setContractAddress(contractAddress);
        session.setContractCode(contractCode);
        session.setStatus("RUNNING");
        session.setStartTime(System.currentTimeMillis());
        sessionMap.put(sessionId, session);
        return sessionId;
    }

    public void addBreakpoint(String codeLine) {
        if (!breakpoints.contains(codeLine)) {
            breakpoints.add(codeLine);
        }
    }

    public DebugResult stepExecute(String sessionId, String currentLine) {
        DebugSession session = sessionMap.get(sessionId);
        DebugResult result = new DebugResult();
        if (session == null || !debugEnabled) {
            result.setSuccess(false);
            result.setMessage("Debug session not found");
            return result;
        }
        result.setSuccess(true);
        result.setCurrentLine(currentLine);
        result.setHitBreakpoint(breakpoints.contains(currentLine));
        if (result.isHitBreakpoint()) {
            session.setStatus("PAUSED");
        }
        return result;
    }

    public void stopDebugSession(String sessionId) {
        DebugSession session = sessionMap.get(sessionId);
        if (session != null) {
            session.setStatus("STOPPED");
            session.setEndTime(System.currentTimeMillis());
        }
    }

    public void setDebugEnabled(boolean enabled) {
        this.debugEnabled = enabled;
    }
}

class DebugSession {
    private String sessionId;
    private String contractAddress;
    private String contractCode;
    private String status;
    private long startTime;
    private long endTime;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getContractAddress() { return contractAddress; }
    public void setContractAddress(String contractAddress) { this.contractAddress = contractAddress; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
}

class DebugResult {
    private boolean success;
    private String message;
    private String currentLine;
    private boolean hitBreakpoint;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCurrentLine() { return currentLine; }
    public void setCurrentLine(String currentLine) { this.currentLine = currentLine; }
    public boolean isHitBreakpoint() { return hitBreakpoint; }
    public void setHitBreakpoint(boolean hitBreakpoint) { this.hitBreakpoint = hitBreakpoint; }
}
