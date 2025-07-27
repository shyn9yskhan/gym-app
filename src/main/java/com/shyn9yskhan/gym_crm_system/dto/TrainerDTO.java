package com.shyn9yskhan.gym_crm_system.dto;

public class TrainerDTO {
    private String firstname;
    private String lastname;
    private String trainingTypeName;

    public TrainerDTO() {
    }

    public TrainerDTO(String firstname, String lastname, String trainingTypeName) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.trainingTypeName = trainingTypeName;
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

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}