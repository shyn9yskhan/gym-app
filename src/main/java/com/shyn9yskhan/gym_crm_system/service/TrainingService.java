package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TrainingDto;
import com.shyn9yskhan.gym_crm_system.domain.Training;

public interface TrainingService {
    String createTraining(TrainingDto trainingDto);
    Training getTraining(String trainingId);
}
