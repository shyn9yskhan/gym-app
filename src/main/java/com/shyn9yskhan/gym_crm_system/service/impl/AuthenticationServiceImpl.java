package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.domain.User;
import com.shyn9yskhan.gym_crm_system.service.AuthenticationService;
import com.shyn9yskhan.gym_crm_system.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private UserService userService;

    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        User user = userService.getUserByUsername(username);
        if (user != null) return user.getPassword().equals(password);
        return false;
    }
}
