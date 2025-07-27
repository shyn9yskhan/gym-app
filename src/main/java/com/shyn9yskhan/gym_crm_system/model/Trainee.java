package com.shyn9yskhan.gym_crm_system.model;

import java.time.LocalDate;

public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;
    private String userId;

    public Trainee() {
        super(null, null, null, null, false);
    }

    public Trainee(String firstname, String lastname, String username, String password, boolean isActive, LocalDate dateOfBirth, String address, String userId) {
        super(firstname, lastname, username, password, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}