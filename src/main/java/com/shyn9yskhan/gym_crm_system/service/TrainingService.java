package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dao.TrainingDAO;
import com.shyn9yskhan.gym_crm_system.dto.TrainingDTO;
import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);
    @Autowired
    private TrainingDAO trainingDAO;

    public Training createTraining(TrainingDTO trainingDTO) {
        logger.info("Creating new training: {}", trainingDTO.getTrainingName());

        String trainingId = RandomGenerator.generateUserId();
        String trainerId = trainingDTO.getTrainerId();
        String trainingName = trainingDTO.getTrainingName();
        TrainingType trainingType = new TrainingType(trainingDTO.getTrainingTypeName());
        LocalDateTime trainingDate = trainingDTO.getTrainingDate();
        Duration trainingDuration = trainingDTO.getTrainingDuration();

        Training training = new Training(trainingId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        Training created = trainingDAO.createTraining(training);

        logger.info("Training created with ID: {}, for trainerId: {}", created.getTrainingId(), created.getTrainerId());
        return created;
    }

    public Training getTraining(String trainingId) {
        logger.info("Fetching training with ID: {}", trainingId);
        Training training = trainingDAO.getTraining(trainingId);

        if (training == null) {
            logger.warn("Training with ID {} not found", trainingId);
        } else {
            logger.info("Training with ID {} retrieved successfully", trainingId);
        }

        return training;
    }
}