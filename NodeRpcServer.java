package com.blockchain.core.rpc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NodeRpcServer {
    private final String serverId;
    private final int port;
    private boolean running;
    private final Map<String, RpcMethodHandler> methodMap;

    public NodeRpcServer(int port) {
        this.port = port;
        this.serverId = UUID.randomUUID().toString();
        this.running = false;
        this.methodMap = new HashMap<>();
        registerDefaultMethods();
    }

    private void registerDefaultMethods() {
        methodMap.put("getBlockHeight", params -> Map.of("height", 1000L));
        methodMap.put("getBalance", params -> Map.of("balance", 0L));
        methodMap.put("sendTx", params -> Map.of("txId", UUID.randomUUID().toString()));
    }

    public void startServer() {
        if (!running) {
            this.running = true;
            System.out.println("RPC Server started on port: " + port);
        }
    }

    public void stopServer() {
        if (running) {
            this.running = false;
            System.out.println("RPC Server stopped");
        }
    }

    public RpcResponse callMethod(String method, Map<String, Object> params) {
        RpcResponse response = new RpcResponse();
        if (!running) {
            response.setCode(500);
            response.setMsg("Server not running");
            return response;
        }
        RpcMethodHandler handler = methodMap.get(method);
        if (handler == null) {
            response.setCode(404);
            response.setMsg("Method not found");
        } else {
            response.setCode(200);
            response.setMsg("success");
            response.setData(handler.handle(params));
        }
        return response;
    }

    public boolean isRunning() {
        return running;
    }
}

interface RpcMethodHandler {
    Map<String, Object> handle(Map<String, Object> params);
}

class RpcResponse {
    private int code;
    private String msg;
    private Map<String, Object> data;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}
