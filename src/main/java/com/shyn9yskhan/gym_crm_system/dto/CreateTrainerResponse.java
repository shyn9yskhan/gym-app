package com.shyn9yskhan.gym_crm_system.dto;

public class CreateTrainerResponse {
    private String username;
    private String password;

    public CreateTrainerResponse() {
    }

    public CreateTrainerResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
