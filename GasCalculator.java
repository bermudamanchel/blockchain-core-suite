package com.blockchain.core.gas;

import java.util.HashMap;
import java.util.Map;

public class GasCalculator {
    private final long baseGasFee;
    private final Map<String, Long> operationGasCost;
    private long gasPrice;
    private final long minGasPrice;

    public GasCalculator(long baseFee, long minPrice) {
        this.baseGasFee = baseFee;
        this.minGasPrice = minPrice;
        this.gasPrice = minPrice;
        this.operationGasCost = new HashMap<>();
        initDefaultCosts();
    }

    private void initDefaultCosts() {
        operationGasCost.put("TRANSFER", 21000L);
        operationGasCost.put("CONTRACT_DEPLOY", 100000L);
        operationGasCost.put("CONTRACT_CALL", 50000L);
        operationGasCost.put("STORE_DATA", 15000L);
    }

    public long calculateOperationGas(String operation) {
        return operationGasCost.getOrDefault(operation, baseGasFee);
    }

    public long calculateTotalFee(String operation) {
        return calculateOperationGas(operation) * gasPrice;
    }

    public void updateGasPrice(long newPrice) {
        if (newPrice >= minGasPrice) {
            this.gasPrice = newPrice;
        }
    }

    public void setOperationCost(String operation, long cost) {
        if (cost > 0) {
            operationGasCost.put(operation, cost);
        }
    }

    public long getCurrentGasPrice() {
        return gasPrice;
    }
}
