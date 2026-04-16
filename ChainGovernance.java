package com.blockchain.core.governance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChainGovernance {
    private final Map<String, Proposal> proposalMap;
    private final Map<String, Integer> voterWeight;
    private final List<String> adminList;

    public ChainGovernance(List<String> adminAccounts) {
        this.adminList = new ArrayList<>(adminAccounts);
        this.proposalMap = new ConcurrentHashMap<>();
        this.voterWeight = new ConcurrentHashMap<>();
    }

    public String createProposal(String creator, String title, String content, String type) {
        if (!adminList.contains(creator)) {
            throw new SecurityException("No permission to create proposal");
        }
        String proposalId = UUID.randomUUID().toString();
        Proposal proposal = new Proposal();
        proposal.setProposalId(proposalId);
        proposal.setTitle(title);
        proposal.setContent(content);
        proposal.setType(type);
        proposal.setCreator(creator);
        proposal.setStatus("PENDING");
        proposal.setCreateTime(System.currentTimeMillis());
        proposalMap.put(proposalId, proposal);
        return proposalId;
    }

    public boolean voteProposal(String voter, String proposalId, boolean agree) {
        Proposal p = proposalMap.get(proposalId);
        if (p == null || !p.getStatus().equals("PENDING")) {
            return false;
        }
        int weight = voterWeight.getOrDefault(voter, 1);
        if (agree) {
            p.setAgreeVotes(p.getAgreeVotes() + weight);
        } else {
            p.setAgainstVotes(p.getAgainstVotes() + weight);
        }
        checkVoteResult(p);
        return true;
    }

    private void checkVoteResult(Proposal p) {
        int total = p.getAgreeVotes() + p.getAgainstVotes();
        if (total >= adminList.size() * 0.8) {
            p.setStatus(p.getAgreeVotes() > p.getAgainstVotes() ? "PASSED" : "REJECTED");
        }
    }

    public void setVoterWeight(String voter, int weight) {
        voterWeight.put(voter, weight);
    }
}

class Proposal {
    private String proposalId;
    private String title;
    private String content;
    private String type;
    private String creator;
    private String status;
    private int agreeVotes;
    private int againstVotes;
    private long createTime;

    public String getProposalId() { return proposalId; }
    public void setProposalId(String proposalId) { this.proposalId = proposalId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getAgreeVotes() { return agreeVotes; }
    public void setAgreeVotes(int agreeVotes) { this.agreeVotes = agreeVotes; }
    public int getAgainstVotes() { return againstVotes; }
    public void setAgainstVotes(int againstVotes) { this.againstVotes = againstVotes; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
}
