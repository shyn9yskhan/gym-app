package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.AppMetrics;
import com.shyn9yskhan.gym_crm_system.domain.User;
import com.shyn9yskhan.gym_crm_system.service.AuthenticationService;
import com.shyn9yskhan.gym_crm_system.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private UserService userService;
    private AppMetrics metrics;

    public AuthenticationServiceImpl(UserService userService, AppMetrics metrics) {
        this.userService = userService;
        this.metrics = metrics;
    }

    @Override
    public boolean authenticate(String username, String password) {
        metrics.authAttempt();
        if (username == null || password == null) {
            metrics.authFailure();
            return false;
        }
        User user = userService.getUserByUsername(username);
        boolean isAuthenticated = user != null && user.getPassword().equals(password);
        if (isAuthenticated) metrics.authSuccess(); else metrics.authFailure();
        return isAuthenticated;
    }

    @Override
    public String changePassword(String username, String oldPassword, String newPassword) {
        return userService.changePassword(username, oldPassword, newPassword);
    }
}
