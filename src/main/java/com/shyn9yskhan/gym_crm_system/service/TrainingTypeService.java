package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;

public interface TrainingTypeService {
    TrainingTypeEntity getTrainingTypeByName(String trainingTypeName);
}
