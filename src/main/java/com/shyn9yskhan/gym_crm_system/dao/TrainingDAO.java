package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Training;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDAO {
    private final Map<String, Training> trainings;

    public TrainingDAO(@Qualifier("trainingStorage") Map<String, Training> trainings) {
        this.trainings = trainings;
    }

    public Training createTraining(Training training) {
        trainings.put(training.getTrainingId(), training);
        return training;
    }

    public boolean updateTraining(String trainingId, Training updatedTraining) {
        if (!trainings.containsKey(trainingId)) {
            return false;
        }
        trainings.put(trainingId, updatedTraining);
        return true;
    }

    public boolean deleteTraining(String trainingId) {
        return trainings.remove(trainingId) != null;
    }

    public Training getTraining(String trainingId) {
        return trainings.get(trainingId);
    }
}