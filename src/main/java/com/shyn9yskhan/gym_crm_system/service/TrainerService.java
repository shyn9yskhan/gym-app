package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.domain.Trainer;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;

public interface TrainerService {
    String createTrainer(TrainerDto trainerDto);
    String updateTrainer(String trainerId, String updatedTrainingTypeName);
    Trainer getTrainer(String trainerId);
    TrainerEntity getTrainerEntity(String trainerId);
    TrainerEntity saveTrainer(TrainerEntity trainerEntity);
}
