package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.*;
import com.shyn9yskhan.gym_crm_system.domain.Trainer;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;

import java.util.List;

public interface TrainerService {
    CreateTrainerResponse createTrainer(TrainerDto trainerDto);
    TrainerProfile updateTrainer(UpdateTrainerRequest updateTrainerRequest);
    Trainer getTrainer(String trainerId);
    TrainerEntity getTrainerEntity(String trainerId);
    TrainerEntity getTrainerEntityByUsername(String username);
    TrainerEntity saveTrainer(TrainerEntity trainerEntity);
    TrainerProfile getTrainerProfileByUsername(String username);
    List<TrainerProfileDto> getNotAssignedOnTraineeActiveTrainers(String username);
    List<TrainerEntity> findAllByUserUsernameIn(List<String> usernames);
    List<GetTrainerTrainingsListResponse> getTrainerTrainings(GetTrainerTrainingsListRequest request);
    String updateTrainerActivation(String username, boolean isActive);
}
