package com.shyn9yskhan.gym_crm_system.model;

public class Trainer extends User {
    private TrainingType specialization;
    private String userId;

    public Trainer() {
        super(null, null, null, null, false);
    }

    public Trainer(String firstname, String lastname, String username, String password, boolean isActive, TrainingType specialization, String userId) {
        super(firstname, lastname, username, password, isActive);
        this.specialization = specialization;
        this.userId = userId;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}