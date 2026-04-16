package com.blockchain.core.did;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DIDCore {
    private final Map<String, DIDDocument> didDocumentMap;
    private final Map<String, List<String>> didAuthMap;
    private final String didPrefix;

    public DIDCore() {
        this.didPrefix = "did:blockchain:";
        this.didDocumentMap = new ConcurrentHashMap<>();
        this.didAuthMap = new ConcurrentHashMap<>();
    }

    public String createDID(String owner, String publicKey) {
        String did = didPrefix + UUID.randomUUID().toString().replace("-", "");
        DIDDocument doc = new DIDDocument();
        doc.setDid(did);
        doc.setOwner(owner);
        doc.setPublicKey(publicKey);
        doc.setCreateTime(System.currentTimeMillis());
        doc.setStatus("ACTIVE");
        didDocumentMap.put(did, doc);
        didAuthMap.put(did, new ArrayList<>());
        return did;
    }

    public boolean addAuthentication(String did, String authMethod) {
        DIDDocument doc = didDocumentMap.get(did);
        List<String> authList = didAuthMap.get(did);
        if (doc == null || authList == null || !doc.getStatus().equals("ACTIVE")) {
            return false;
        }
        if (!authList.contains(authMethod)) {
            authList.add(authMethod);
        }
        return true;
    }

    public boolean verifyDID(String did, String signature) {
        DIDDocument doc = didDocumentMap.get(did);
        return doc != null && doc.getStatus().equals("ACTIVE") && signature != null && signature.length() > 30;
    }

    public boolean revokeDID(String did) {
        DIDDocument doc = didDocumentMap.get(did);
        if (doc != null && doc.getStatus().equals("ACTIVE")) {
            doc.setStatus("REVOKED");
            return true;
        }
        return false;
    }

    public DIDDocument getDIDDocument(String did) {
        return didDocumentMap.get(did);
    }
}

class DIDDocument {
    private String did;
    private String owner;
    private String publicKey;
    private String status;
    private long createTime;

    public String getDid() { return did; }
    public void setDid(String did) { this.did = did; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
}
