package com.shyn9yskhan.gym_crm_system.service;

public interface AuthenticationService {
    boolean authenticate(String username, String password);
    String changePassword(String username, String oldPassword, String newPassword);
}
