package com.shyn9yskhan.gym_crm_system.dto;

import java.time.LocalDate;
import java.util.List;

public class TraineeProfile {
    private String username;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerProfileDto> trainers;

    public TraineeProfile() {}

    public TraineeProfile(String username, String firstname, String lastname, LocalDate dateOfBirth, String address, boolean isActive, List<TrainerProfileDto> trainers) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.isActive = isActive;
        this.trainers = trainers;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<TrainerProfileDto> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<TrainerProfileDto> trainers) {
        this.trainers = trainers;
    }
}
