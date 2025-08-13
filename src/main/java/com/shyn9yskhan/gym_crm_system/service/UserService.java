package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.domain.User;

public interface UserService {
    UserCreationResult createUser(String firstname, String lastname);
    User getUserByUsername(String username);
    String changePassword(String userId, String newPassword);
    boolean setActive(String userId, boolean active);
}
