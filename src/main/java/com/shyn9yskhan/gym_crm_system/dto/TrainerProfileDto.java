package com.shyn9yskhan.gym_crm_system.dto;

public class TrainerProfileDto {
    private String username;
    private String firstname;
    private String lastname;
    private TrainingTypeDto specialization;

    public TrainerProfileDto() {
    }

    public TrainerProfileDto(String username, String firstname, String lastname, TrainingTypeDto specialization) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.specialization = specialization;
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
}
