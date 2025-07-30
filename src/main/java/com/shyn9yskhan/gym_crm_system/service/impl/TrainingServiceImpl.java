package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.dto.TrainingDto;
import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.repository.TrainingRepository;
import com.shyn9yskhan.gym_crm_system.service.RandomGenerator;
import com.shyn9yskhan.gym_crm_system.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public Training createTraining(TrainingDto trainingDto) {
        logger.info("Creating new training: {}", trainingDto.getTrainingName());

        String trainingId = RandomGenerator.generateUserId();
        String trainerId = trainingDto.getTrainerId();
        String trainingName = trainingDto.getTrainingName();
        TrainingType trainingType = new TrainingType(trainingDto.getTrainingTypeName());
        LocalDateTime trainingDate = trainingDto.getTrainingDate();
        Duration trainingDuration = trainingDto.getTrainingDuration();

        Training training = new Training(trainingId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        Training created = trainingRepository.createTraining(training);

        logger.info("Training created with ID: {}, for trainerId: {}", created.getId(), created.getTrainerId());
        return created;
    }

    public Training getTraining(String trainingId) {
        logger.info("Fetching training with ID: {}", trainingId);
        Training training = trainingRepository.getTraining(trainingId);

        if (training == null) {
            logger.warn("Training with ID {} not found", trainingId);
        } else {
            logger.info("Training with ID {} retrieved successfully", trainingId);
        }

        return training;
    }
}
