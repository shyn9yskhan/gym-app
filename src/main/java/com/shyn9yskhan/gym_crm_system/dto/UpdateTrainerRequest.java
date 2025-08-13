package com.shyn9yskhan.gym_crm_system.dto;

public class UpdateTrainerRequest {
    private String trainingTypeName;

    public UpdateTrainerRequest() {
    }

    public UpdateTrainerRequest(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}
