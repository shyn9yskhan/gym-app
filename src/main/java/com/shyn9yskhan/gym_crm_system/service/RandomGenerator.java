package com.shyn9yskhan.gym_crm_system.service;

import java.util.UUID;

public class RandomGenerator {
    public static String generateUserId() {
        return UUID.randomUUID().toString();
    }

    public static String generatePassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
