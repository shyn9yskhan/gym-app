package com.shyn9yskhan.gym_crm_system.security;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTokenBlacklist implements TokenBlacklist {
    private final Set<String> black = ConcurrentHashMap.newKeySet();

    @Override
    public void blacklist(String token) {
        if (token != null) black.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return token != null && black.contains(token);
    }
}