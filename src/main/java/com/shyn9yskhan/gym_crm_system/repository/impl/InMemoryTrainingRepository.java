package com.shyn9yskhan.gym_crm_system.repository.impl;

import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.repository.TrainingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class InMemoryTrainingRepository implements TrainingRepository {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryTrainingRepository.class);
    private final Map<String, Training> trainings;

    public InMemoryTrainingRepository(@Qualifier("trainingStorage") Map<String, Training> trainings) {
        this.trainings = trainings;
        logger.debug("TrainingDAO initialized with training storage.");
    }

    @Override
    public Training createTraining(Training training) {
        trainings.put(training.getId(), training);
        logger.info("Created training with ID: {}", training.getId());
        return training;
    }

    @Override
    public Training updateTraining(String trainingId, Training updatedTraining) {
        logger.debug("Attempting to update training with ID: {}", trainingId);
        if (!trainings.containsKey(trainingId)) {
            logger.warn("Training with ID: {} not found. Update failed.", trainingId);
            return null;
        }
        trainings.put(trainingId, updatedTraining);
        logger.info("Updated training with ID: {}", trainingId);
        return updatedTraining;
    }

    @Override
    public Training deleteTraining(String trainingId) {
        Training removedTraining = trainings.remove(trainingId);
        if (removedTraining != null) {
            logger.info("Deleted training with ID: {}", trainingId);
        } else {
            logger.warn("Training with ID: {} not found. Delete failed.", trainingId);
        }
        return removedTraining;
    }

    @Override
    public Training getTraining(String trainingId) {
        logger.debug("Retrieving training with ID: {}", trainingId);
        return trainings.get(trainingId);
    }
}
