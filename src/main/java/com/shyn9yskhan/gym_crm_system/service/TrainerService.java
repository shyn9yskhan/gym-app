package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.model.Trainer;

public interface TrainerService {
    Trainer createTrainer(TrainerDto trainerDto);
    Trainer updateTrainer(String trainerId, TrainerDto trainerDto);
    Trainer getTrainer(String trainerId);
}
