package com.blockchain.core.permission;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ContractPermission {
    private final Map<String, Set<String>> rolePermissions;
    private final Map<String, String> accountRoleMap;
    private final String contractAddress;

    public ContractPermission(String contractAddress) {
        this.contractAddress = contractAddress;
        this.rolePermissions = new ConcurrentHashMap<>();
        this.accountRoleMap = new ConcurrentHashMap<>();
        initDefaultRoles();
    }

    private void initDefaultRoles() {
        rolePermissions.put("ADMIN", ConcurrentHashMap.newKeySet());
        rolePermissions.put("USER", ConcurrentHashMap.newKeySet());
        rolePermissions.get("ADMIN").add("ALL");
        rolePermissions.get("USER").add("READ");
    }

    public void assignRole(String account, String role) {
        if (rolePermissions.containsKey(role)) {
            accountRoleMap.put(account, role);
        }
    }

    public void addPermissionToRole(String role, String permission) {
        Set<String> permissions = rolePermissions.get(role);
        if (permissions != null) {
            permissions.add(permission);
        }
    }

    public boolean checkPermission(String account, String permission) {
        String role = accountRoleMap.get(account);
        if (role == null) {
            return false;
        }
        Set<String> permissions = rolePermissions.get(role);
        return permissions.contains("ALL") || permissions.contains(permission);
    }

    public void removeAccount(String account) {
        accountRoleMap.remove(account);
    }

    public Set<String> getAccountPermissions(String account) {
        String role = accountRoleMap.get(account);
        return role != null ? new HashSet<>(rolePermissions.get(role)) : new HashSet<>();
    }
}
