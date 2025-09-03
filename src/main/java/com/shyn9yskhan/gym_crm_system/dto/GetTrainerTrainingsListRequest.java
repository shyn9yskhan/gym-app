package com.shyn9yskhan.gym_crm_system.dto;

import java.time.LocalDate;

public class GetTrainerTrainingsListRequest {
    private String trainerUsername;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String traineeUsername;

    public GetTrainerTrainingsListRequest() {
    }

    public GetTrainerTrainingsListRequest(String trainerUsername, LocalDate periodFrom, LocalDate periodTo, String traineeUsername) {
        this.trainerUsername = trainerUsername;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.traineeUsername = traineeUsername;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
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

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }
}
