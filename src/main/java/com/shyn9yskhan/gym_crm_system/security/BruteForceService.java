package com.shyn9yskhan.gym_crm_system.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BruteForceService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_MS = 5 * 60 * 1000L;
    private final ConcurrentHashMap<String, Attempt> map = new ConcurrentHashMap<>();

    public void recordFailure(String username) {
        map.compute(username, (k, v) -> {
            if (v == null || (v.blockedUntil != null && v.blockedUntil.isBefore(Instant.now()))) {
                return new Attempt(1, null);
            }
            int attempts = v.attempts + 1;
            Instant blocked = attempts >= MAX_ATTEMPTS ? Instant.now().plusMillis(BLOCK_MS) : null;
            return new Attempt(attempts, blocked);
        });
    }

    public void recordSuccess(String username) {
        map.remove(username);
    }

    public boolean isBlocked(String username) {
        Attempt a = map.get(username);
        if (a == null) return false;
        return a.blockedUntil != null && a.blockedUntil.isAfter(Instant.now());
    }

    private static class Attempt {
        final int attempts;
        final Instant blockedUntil;
        Attempt(int attempts, Instant blockedUntil) { this.attempts = attempts; this.blockedUntil = blockedUntil; }
    }
}
