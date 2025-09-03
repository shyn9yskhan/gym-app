package com.shyn9yskhan.gym_crm_system.dto;

import java.time.LocalDate;

public class GetTraineeTrainingsListRequest {
    private String username;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerUsername;
    private TrainingTypeDto trainingType;

    public GetTraineeTrainingsListRequest() {
    }

    public GetTraineeTrainingsListRequest(String username, LocalDate periodFrom, LocalDate periodTo, String trainerUsername, TrainingTypeDto trainingType) {
        this.username = username;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.trainerUsername = trainerUsername;
        this.trainingType = trainingType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
    }

    public LocalDate getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public TrainingTypeDto getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingTypeDto trainingType) {
        this.trainingType = trainingType;
    }
}
