package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);
    private final Map<String, Training> trainings;

    public TrainingDAO(@Qualifier("trainingStorage") Map<String, Training> trainings) {
        this.trainings = trainings;
        logger.debug("TrainingDAO initialized with training storage.");
    }

    public Training createTraining(Training training) {
        trainings.put(training.getTrainingId(), training);
        logger.info("Created training with ID: {}", training.getTrainingId());
        return training;
    }

    public boolean updateTraining(String trainingId, Training updatedTraining) {
        logger.debug("Attempting to update training with ID: {}", trainingId);
        if (!trainings.containsKey(trainingId)) {
            logger.warn("Training with ID: {} not found. Update failed.", trainingId);
            return false;
        }
        trainings.put(trainingId, updatedTraining);
        logger.info("Updated training with ID: {}", trainingId);
        return true;
    }

    public boolean deleteTraining(String trainingId) {
        boolean removed = trainings.remove(trainingId) != null;
        if (removed) {
            logger.info("Deleted training with ID: {}", trainingId);
        } else {
            logger.warn("Training with ID: {} not found. Delete failed.", trainingId);
        }
        return removed;
    }

    public Training getTraining(String trainingId) {
        logger.debug("Retrieving training with ID: {}", trainingId);
        return trainings.get(trainingId);
    }
}