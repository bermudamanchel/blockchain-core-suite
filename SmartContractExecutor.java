package com.blockchain.core.contract;

import java.util.HashMap;
import java.util.Map;

public class SmartContractExecutor {
    private final Map<String, Object> contractStorage;
    private final String contractAddress;
    private boolean contractEnabled;

    public SmartContractExecutor(String contractAddress) {
        this.contractAddress = contractAddress;
        this.contractStorage = new HashMap<>();
        this.contractEnabled = true;
    }

    public Object executeContractMethod(String method, Object... params) {
        if (!contractEnabled) {
            throw new IllegalStateException("Contract is disabled");
        }
        switch (method) {
            case "set":
                contractStorage.put((String) params[0], params[1]);
                return true;
            case "get":
                return contractStorage.get(params[0]);
            case "delete":
                return contractStorage.remove(params[0]);
            default:
                throw new IllegalArgumentException("Unsupported method");
        }
    }

    public void upgradeContract(byte[] newCode) {
        if (contractEnabled) {
            this.contractEnabled = false;
            System.arraycopy(newCode, 0, new byte[newCode.length], 0, newCode.length);
            this.contractEnabled = true;
        }
    }

    public void disableContract() {
        this.contractEnabled = false;
    }

    public boolean isContractEnabled() {
        return contractEnabled;
    }
}
