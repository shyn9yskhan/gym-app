package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Trainee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TraineeDAO {
    private final Map<String, Trainee> trainees;

    public TraineeDAO(@Qualifier("traineeStorage") Map<String, Trainee> trainees) {
        this.trainees = trainees;
    }

    public Trainee createTrainee(Trainee trainee) {
        trainees.put(trainee.getUserId(), trainee);
        return trainee;
    }

    public boolean updateTrainee(String traineeId, Trainee updatedTrainee) {
        if (!trainees.containsKey(traineeId)) {
            return false;
        }
        trainees.put(traineeId, updatedTrainee);
        return true;
    }

    public boolean deleteTrainee(String traineeId) {
        return trainees.remove(traineeId) != null;
    }

    public Trainee getTrainee(String traineeId) {
        return trainees.get(traineeId);
    }

    public boolean existsByUsername(String username) {
        for (Trainee trainee : trainees.values()) {
            if (Objects.equals(trainee.getUsername(), username)) {
                return true;
            }
        }
        return false;
    }

    public List<String> findUsernamesByBase(String base) {
        List<String> matches = new ArrayList<>();
        for (Trainee trainee : trainees.values()) {
            String username = trainee.getUsername();
            if (username != null && username.startsWith(base)) {
                matches.add(username);
            }
        }
        return matches;
    }
}