package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.model.Training;

public interface TrainingRepository {
    Training createTraining(Training training);
    Training updateTraining(String trainingId, Training updatedTraining);
    Training deleteTraining(String trainingId);
    Training getTraining(String trainingId);
}
