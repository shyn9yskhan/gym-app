package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TrainingDto;
import com.shyn9yskhan.gym_crm_system.model.Training;

public interface TrainingService {
    Training createTraining(TrainingDto trainingDto);
    Training getTraining(String trainingId);
}
