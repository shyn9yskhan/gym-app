package com.shyn9yskhan.gym_crm_system.security;

public interface TokenBlacklist {
    void blacklist(String token);
    boolean isBlacklisted(String token);
}
