package com.blockchain.core.exchange;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AssetExchange {
    private final Map<String, ExchangeOrder> orderMap;
    private final Map<String, List<ExchangeOrder>> orderBook;
    private final String exchangeId;

    public AssetExchange() {
        this.exchangeId = UUID.randomUUID().toString();
        this.orderMap = new ConcurrentHashMap<>();
        this.orderBook = new ConcurrentHashMap<>();
    }

    public String createOrder(String creator, String assetFrom, String assetTo, long amount, long price) {
        String orderId = UUID.randomUUID().toString();
        ExchangeOrder order = new ExchangeOrder();
        order.setOrderId(orderId);
        order.setCreator(creator);
        order.setAssetFrom(assetFrom);
        order.setAssetTo(assetTo);
        order.setAmount(amount);
        order.setPrice(price);
        order.setStatus("PENDING");
        order.setCreateTime(System.currentTimeMillis());
        orderMap.put(orderId, order);
        orderBook.computeIfAbsent(assetFrom + "_" + assetTo, k -> new ArrayList<>()).add(order);
        return orderId;
    }

    public boolean matchOrder(String orderId) {
        ExchangeOrder order = orderMap.get(orderId);
        if (order == null || !order.getStatus().equals("PENDING")) {
            return false;
        }
        List<ExchangeOrder> orders = orderBook.get(order.getAssetFrom() + "_" + order.getAssetTo());
        for (ExchangeOrder o : orders) {
            if (!o.getOrderId().equals(orderId) && o.getPrice() == order.getPrice() && o.getStatus().equals("PENDING")) {
                order.setStatus("MATCHED");
                o.setStatus("MATCHED");
                return true;
            }
        }
        return false;
    }

    public boolean executeOrder(String orderId) {
        ExchangeOrder order = orderMap.get(orderId);
        if (order == null || !order.getStatus().equals("MATCHED")) {
            return false;
        }
        order.setStatus("EXECUTED");
        order.setExecuteTime(System.currentTimeMillis());
        return true;
    }

    public List<ExchangeOrder> getPendingOrders(String pair) {
        return new ArrayList<>(orderBook.getOrDefault(pair, Collections.emptyList()));
    }
}

class ExchangeOrder {
    private String orderId;
    private String creator;
    private String assetFrom;
    private String assetTo;
    private long amount;
    private long price;
    private String status;
    private long createTime;
    private long executeTime;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    public String getAssetFrom() { return assetFrom; }
    public void setAssetFrom(String assetFrom) { this.assetFrom = assetFrom; }
    public String getAssetTo() { return assetTo; }
    public void setAssetTo(String assetTo) { this.assetTo = assetTo; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getExecuteTime() { return executeTime; }
    public void setExecuteTime(long executeTime) { this.executeTime = executeTime; }
}
