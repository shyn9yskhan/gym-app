package com.shyn9yskhan.gym_crm_system.dto;

public class TrainingTypeDto {
    private String id;
    private String trainingTypeName;

    public TrainingTypeDto() {
    }

    public TrainingTypeDto(String id, String trainingTypeName) {
        this.id = id;
        this.trainingTypeName = trainingTypeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}
