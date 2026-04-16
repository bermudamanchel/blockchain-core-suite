package com.blockchain.core.reward;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockReward {
    private final long baseReward;
    private final double minerFeeRatio;
    private final Map<String, Long> rewardBalanceMap;
    private final long halvingInterval;

    public BlockReward(long base, double feeRatio, long halving) {
        this.baseReward = base;
        this.minerFeeRatio = feeRatio;
        this.halvingInterval = halving;
        this.rewardBalanceMap = new HashMap<>();
    }

    public long calculateBlockReward(long blockHeight) {
        int halvingTimes = (int) (blockHeight / halvingInterval);
        return baseReward / (1 << halvingTimes);
    }

    public long calculateMinerFee(long totalFee) {
        return (long) (totalFee * minerFeeRatio);
    }

    public String distributeReward(String miner, long blockHeight, long totalFee) {
        long blockReward = calculateBlockReward(blockHeight);
        long minerFee = calculateMinerFee(totalFee);
        long totalReward = blockReward + minerFee;
        
        rewardBalanceMap.put(miner, rewardBalanceMap.getOrDefault(miner, 0L) + totalReward);
        
        String rewardId = UUID.randomUUID().toString();
        RewardRecord record = new RewardRecord();
        record.setRewardId(rewardId);
        record.setMiner(miner);
        record.setBlockHeight(blockHeight);
        record.setBlockReward(blockReward);
        record.setMinerFee(minerFee);
        record.setTotalReward(totalReward);
        record.setDistributeTime(System.currentTimeMillis());
        return rewardId;
    }

    public long getMinerBalance(String miner) {
        return rewardBalanceMap.getOrDefault(miner, 0L);
    }
}

class RewardRecord {
    private String rewardId;
    private String miner;
    private long blockHeight;
    private long blockReward;
    private long minerFee;
    private long totalReward;
    private long distributeTime;

    public String getRewardId() { return rewardId; }
    public void setRewardId(String rewardId) { this.rewardId = rewardId; }
    public String getMiner() { return miner; }
    public void setMiner(String miner) { this.miner = miner; }
    public long getBlockHeight() { return blockHeight; }
    public void setBlockHeight(long blockHeight) { this.blockHeight = blockHeight; }
    public long getBlockReward() { return blockReward; }
    public void setBlockReward(long blockReward) { this.blockReward = blockReward; }
    public long getMinerFee() { return minerFee; }
    public void setMinerFee(long minerFee) { this.minerFee = minerFee; }
    public long getTotalReward() { return totalReward; }
    public void setTotalReward(long totalReward) { this.totalReward = totalReward; }
    public long getDistributeTime() { return distributeTime; }
    public void setDistributeTime(long distributeTime) { this.distributeTime = distributeTime; }
}
