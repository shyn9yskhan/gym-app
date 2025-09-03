package com.shyn9yskhan.gym_crm_system.dto;

import java.util.List;

public class TrainerProfile {
    private String username;
    private String firstname;
    private String lastname;
    private TrainingTypeDto specialization;
    private boolean isActive;
    private List<TraineeProfileDto> trainees;

    public TrainerProfile() {}

    public TrainerProfile(String username, String firstname, String lastname, TrainingTypeDto specialization, boolean isActive, List<TraineeProfileDto> trainees) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.specialization = specialization;
        this.isActive = isActive;
        this.trainees = trainees;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public TrainingTypeDto getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingTypeDto specialization) {
        this.specialization = specialization;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<TraineeProfileDto> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<TraineeProfileDto> trainees) {
        this.trainees = trainees;
    }
}
