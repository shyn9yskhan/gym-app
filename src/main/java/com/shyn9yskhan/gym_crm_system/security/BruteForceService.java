package com.shyn9yskhan.gym_crm_system.security;

import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BruteForceService {
    private static final int MAX_ATTEMPTS = 3;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(5);
    private final ConcurrentHashMap<String, Attempt> attemptsByUsername = new ConcurrentHashMap<>();

    public void recordFailure(String username) {
        attemptsByUsername.compute(username, (key, existingAttempt) -> {
            Instant now = Instant.now();
            if (existingAttempt == null || isBlockExpired(existingAttempt, now)) {
                return new Attempt(1, null);
            }
            int newCount = existingAttempt.attemptsCount + 1;
            Instant blockedUntil = newCount >= MAX_ATTEMPTS ? now.plus(BLOCK_DURATION) : null;
            return new Attempt(newCount, blockedUntil);
        });
    }

    public void recordSuccess(String username) {
        attemptsByUsername.remove(username);
    }

    public boolean isBlocked(String username) {
        Attempt attempt = attemptsByUsername.get(username);
        return isCurrentlyBlocked(attempt, Instant.now());
    }

    private boolean isCurrentlyBlocked(Attempt attempt, Instant now) {
        if (attempt == null) return false;
        Instant blockedUntil = attempt.blockedUntil;
        return blockedUntil != null && blockedUntil.isAfter(now);
    }

    private boolean isBlockExpired(Attempt attempt, Instant now) {
        if (attempt == null) return false;
        Instant blockedUntil = attempt.blockedUntil;
        return blockedUntil != null && blockedUntil.isBefore(now);
    }

    private static final class Attempt {
        final int attemptsCount;
        final Instant blockedUntil;

        Attempt(int attemptsCount, Instant blockedUntil) {
            this.attemptsCount = attemptsCount;
            this.blockedUntil = blockedUntil;
        }
    }
}
