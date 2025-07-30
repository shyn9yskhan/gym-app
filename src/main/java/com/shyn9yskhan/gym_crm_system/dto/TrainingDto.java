package com.shyn9yskhan.gym_crm_system.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public class TrainingDto {
    private String trainerId;
    private String trainingName;
    private String trainingTypeName;
    private LocalDateTime trainingDate;
    private Duration trainingDuration;

    public TrainingDto() {
    }

    public TrainingDto(String trainerId, String trainingName, String trainingTypeName, LocalDateTime trainingDate, Duration trainingDuration) {
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingTypeName = trainingTypeName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }

    public LocalDateTime getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDateTime trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Duration getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Duration trainingDuration) {
        this.trainingDuration = trainingDuration;
    }
}
