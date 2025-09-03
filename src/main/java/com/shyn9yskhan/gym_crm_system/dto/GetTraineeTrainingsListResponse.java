package com.shyn9yskhan.gym_crm_system.dto;

import java.time.LocalDate;

public class GetTraineeTrainingsListResponse {
    private String trainingName;
    private LocalDate trainingDate;
    private TrainingTypeDto trainingType;
    private int trainingDuration;
    private String trainerUsername;

    public GetTraineeTrainingsListResponse() {
    }

    public GetTraineeTrainingsListResponse(String trainingName, LocalDate trainingDate, TrainingTypeDto trainingType, int trainingDuration, String trainerUsername) {
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingType = trainingType;
        this.trainingDuration = trainingDuration;
        this.trainerUsername = trainerUsername;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public TrainingTypeDto getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingTypeDto trainingType) {
        this.trainingType = trainingType;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(int trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }
}
