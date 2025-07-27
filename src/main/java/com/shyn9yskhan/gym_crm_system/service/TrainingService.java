package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dao.TrainingDAO;
import com.shyn9yskhan.gym_crm_system.dto.TrainingDTO;
import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TrainingService {

    @Autowired
    private TrainingDAO trainingDAO;

    public Training createTraining(TrainingDTO trainingDTO) {

        String trainingId = RandomGenerator.generateUserId();
        String trainerId = trainingDTO.getTrainerId();
        String trainingName = trainingDTO.getTrainingName();
        TrainingType trainingType = new TrainingType(trainingDTO.getTrainingTypeName());
        LocalDateTime trainingDate = trainingDTO.getTrainingDate();
        Duration trainingDuration = trainingDTO.getTrainingDuration();

        Training training = new Training(trainingId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

        return trainingDAO.createTraining(training);
    }

    public Training getTraining(String trainingId) {
        return trainingDAO.getTraining(trainingId);
    }
}