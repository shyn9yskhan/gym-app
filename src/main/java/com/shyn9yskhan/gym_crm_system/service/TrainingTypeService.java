package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TrainingTypeDto;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;

import java.util.List;

public interface TrainingTypeService {
    TrainingTypeEntity getTrainingTypeByName(String trainingTypeName);
    List<TrainingTypeDto> getAllTrainingTypes();
}
