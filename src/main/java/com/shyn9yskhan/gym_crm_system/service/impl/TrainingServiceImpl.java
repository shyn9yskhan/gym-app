package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.domain.TrainingType;
import com.shyn9yskhan.gym_crm_system.dto.TrainingDto;
import com.shyn9yskhan.gym_crm_system.domain.Training;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainingRepository;
import com.shyn9yskhan.gym_crm_system.service.TraineeService;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
import com.shyn9yskhan.gym_crm_system.service.TrainingService;
import com.shyn9yskhan.gym_crm_system.service.TrainingTypeService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private TrainingRepository trainingRepository;
    TraineeService traineeService;
    TrainerService trainerService;
    TrainingTypeService trainingTypeService;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeService traineeService, TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    @Transactional
    public String createTraining(TrainingDto trainingDto) {
        logger.info("Creating new training: {}", trainingDto.getTrainingName());

        String traineeId = trainingDto.getTraineeId();
        String trainerId = trainingDto.getTrainerId();
        String trainingName = trainingDto.getTrainingName();
        String trainingTypeName = trainingDto.getTrainingTypeName();
        LocalDate trainingDate = trainingDto.getTrainingDate();
        int trainingDuration = trainingDto.getTrainingDuration();

        TraineeEntity traineeEntity = traineeService.getTraineeEntity(traineeId);
        TrainerEntity trainerEntity = trainerService.getTrainerEntity(trainerId);
        TrainingTypeEntity trainingTypeEntity = trainingTypeService.getTrainingTypeByName(trainingTypeName);

        if (traineeEntity != null && trainerEntity != null && trainingTypeEntity != null) {
            TrainingEntity trainingEntity = new TrainingEntity();

            trainingEntity.setTrainee(traineeEntity);
            trainingEntity.setTrainer(trainerEntity);
            trainingEntity.setTrainingName(trainingName);
            trainingEntity.setTrainingType(trainingTypeEntity);
            trainingEntity.setTrainingDate(trainingDate);
            trainingEntity.setTrainingDuration(trainingDuration);

            traineeEntity.addTrainer(trainerEntity);
            trainerEntity.addTrainee(traineeEntity);

            trainingRepository.save(trainingEntity);
            traineeService.saveTrainee(traineeEntity);
            trainerService.saveTrainer(trainerEntity);

            logger.info("Training created with ID: {}", trainingEntity.getId());
            return trainingEntity.getId();
        }

        return null;
    }

    @Override
    public Training getTraining(String trainingId) {
        logger.info("Fetching training with ID: {}", trainingId);
        Optional<TrainingEntity> optionalTrainingEntity = trainingRepository.findById(trainingId);
        if (optionalTrainingEntity.isEmpty()) {
            logger.warn("Training with ID {} not found", trainingId);
            return null;
        }
        TrainingEntity trainingEntity = optionalTrainingEntity.get();
        TraineeEntity traineeEntity = trainingEntity.getTrainee();
        TrainerEntity trainerEntity = trainingEntity.getTrainer();
        String trainingName = trainingEntity.getTrainingName();
        TrainingTypeEntity trainingTypeEntity = trainingEntity.getTrainingType();
        LocalDate trainingDate = trainingEntity.getTrainingDate();
        int trainingDuration = trainingEntity.getTrainingDuration();

        Training training = new Training();
        training.setTraineeId(traineeEntity.getId());
        training.setTrainerId(trainerEntity.getId());
        training.setName(trainingName);
        training.setType(new TrainingType(trainingTypeEntity.getTrainingTypeName()));
        training.setDate(trainingDate);
        training.setDuration(trainingDuration);

        return training;
    }
}
