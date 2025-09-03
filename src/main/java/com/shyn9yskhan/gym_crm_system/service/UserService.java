package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.domain.User;
import com.shyn9yskhan.gym_crm_system.dto.UserDto;

public interface UserService {
    UserCreationResult createUser(String firstname, String lastname);
    UserDto updateUser(UserDto userDto);
    String deleteUser(String username);
    User getUserByUsername(String username);
    String changePassword(String username, String oldPassword, String newPassword);
    String setActive(String userId, boolean active);
    String setActiveByUsername(String username, boolean isActive);
}
