package com.shyn9yskhan.gym_crm_system.dto;

import java.time.LocalDate;

public class GetTrainerTrainingsListResponse {
    private String trainingName;
    private LocalDate trainingDate;
    private TrainingTypeDto trainingType;
    private int TrainingDuration;
    private String traineeUsername;

    public GetTrainerTrainingsListResponse() {
    }

    public GetTrainerTrainingsListResponse(String trainingName, LocalDate trainingDate, TrainingTypeDto trainingType, int trainingDuration, String traineeUsername) {
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingType = trainingType;
        TrainingDuration = trainingDuration;
        this.traineeUsername = traineeUsername;
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
        return TrainingDuration;
    }

    public void setTrainingDuration(int trainingDuration) {
        TrainingDuration = trainingDuration;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }
}
