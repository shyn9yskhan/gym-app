package com.shyn9yskhan.gym_crm_system.dto;

import java.util.List;

public class UpdateTraineesTrainerListRequest {
    private String traineeUsername;
    private List<String> trainers;

    public UpdateTraineesTrainerListRequest() {
    }

    public UpdateTraineesTrainerListRequest(String traineeUsername, List<String> trainers) {
        this.traineeUsername = traineeUsername;
        this.trainers = trainers;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public List<String> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<String> trainers) {
        this.trainers = trainers;
    }
}
