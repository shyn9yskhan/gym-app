package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.dto.TrainingTypeDto;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainingTypeRepository;
import com.shyn9yskhan.gym_crm_system.service.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<TrainingTypeDto> getAllTrainingTypes() {
        List<TrainingTypeEntity> trainingTypeEntities = trainingTypeRepository.findAll();
        List<TrainingTypeDto> trainingTypeDtoList = new ArrayList<>();
        for (TrainingTypeEntity trainingTypeEntity : trainingTypeEntities) {
            TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
            trainingTypeDto.setId(trainingTypeEntity.getId());
            trainingTypeDto.setTrainingTypeName(trainingTypeEntity.getTrainingTypeName());
            trainingTypeDtoList.add(trainingTypeDto);
        }
        return trainingTypeDtoList;
    }
}
