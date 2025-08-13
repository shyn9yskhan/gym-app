package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainingTypeRepository;
import com.shyn9yskhan.gym_crm_system.service.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public TrainingTypeEntity getTrainingTypeByName(String trainingTypeName) {
        Optional<TrainingTypeEntity> optionalTrainingTypeEntity = trainingTypeRepository.findByTrainingTypeName(trainingTypeName);
        return optionalTrainingTypeEntity.orElse(null);
    }
}
