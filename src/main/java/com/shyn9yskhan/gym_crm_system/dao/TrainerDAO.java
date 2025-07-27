package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Trainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrainerDAO {
    private final Map<String, Trainer> trainers;

    public TrainerDAO(@Qualifier("trainerStorage") Map<String, Trainer> trainers) {
        this.trainers = trainers;
    }

    public Trainer createTrainer(Trainer trainer) {
        trainers.put(trainer.getUserId(), trainer);
        return trainer;
    }

    public boolean updateTrainer(String trainerId, Trainer updatedTrainer) {
        if (!trainers.containsKey(trainerId)) {
            return false;
        }
        trainers.put(trainerId, updatedTrainer);
        return true;
    }

    public boolean deleteTrainer(String trainerId) {
        return trainers.remove(trainerId) != null;
    }

    public Trainer getTrainer(String trainerId) {
        return trainers.get(trainerId);
    }

    public boolean existsByUsername(String username) {
        for (Trainer trainer : trainers.values()) {
            if (Objects.equals(trainer.getUsername(), username)) {
                return true;
            }
        }
        return false;
    }

    public List<String> findUsernamesByBase(String base) {
        List<String> matches = new ArrayList<>();
        for (Trainer trainer : trainers.values()) {
            String username = trainer.getUsername();
            if (username != null && username.startsWith(base)) {
                matches.add(username);
            }
        }
        return matches;
    }
}
