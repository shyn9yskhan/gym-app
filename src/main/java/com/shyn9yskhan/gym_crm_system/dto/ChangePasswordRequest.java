package com.shyn9yskhan.gym_crm_system.dto;

public record ChangePasswordRequest(String username, String oldPassword, String newPassword) {
}
