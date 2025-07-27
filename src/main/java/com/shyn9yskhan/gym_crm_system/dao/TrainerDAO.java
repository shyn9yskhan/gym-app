package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrainerDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);
    private final Map<String, Trainer> trainers;

    public TrainerDAO(@Qualifier("trainerStorage") Map<String, Trainer> trainers) {
        this.trainers = trainers;
        logger.debug("TrainerDAO initialized with trainer storage.");
    }

    public Trainer createTrainer(Trainer trainer) {
        trainers.put(trainer.getUserId(), trainer);
        logger.info("Created trainer with ID: {}", trainer.getUserId());
        return trainer;
    }

    public boolean updateTrainer(String trainerId, Trainer updatedTrainer) {
        logger.debug("Attempting to update trainer with ID: {}", trainerId);
        if (!trainers.containsKey(trainerId)) {
            logger.warn("Trainer with ID: {} not found. Update failed.", trainerId);
            return false;
        }
        trainers.put(trainerId, updatedTrainer);
        logger.info("Updated trainer with ID: {}", trainerId);
        return true;
    }

    public boolean deleteTrainer(String trainerId) {
        boolean removed = trainers.remove(trainerId) != null;
        if (removed) {
            logger.info("Deleted trainer with ID: {}", trainerId);
        } else {
            logger.warn("Trainer with ID: {} not found. Delete failed.", trainerId);
        }
        return removed;
    }

    public Trainer getTrainer(String trainerId) {
        logger.debug("Retrieving trainer with ID: {}", trainerId);
        return trainers.get(trainerId);
    }

    public boolean existsByUsername(String username) {
        logger.debug("Checking if trainer exists with username: {}", username);
        for (Trainer trainer : trainers.values()) {
            if (Objects.equals(trainer.getUsername(), username)) {
                logger.debug("Trainer found with username: {}", username);
                return true;
            }
        }
        logger.debug("No trainer found with username: {}", username);
        return false;
    }

    public List<String> findUsernamesByBase(String base) {
        logger.debug("Finding trainer usernames starting with: {}", base);
        List<String> matches = new ArrayList<>();
        for (Trainer trainer : trainers.values()) {
            String username = trainer.getUsername();
            if (username != null && username.startsWith(base)) {
                matches.add(username);
            }
        }
        logger.debug("Found {} usernames starting with: {}", matches.size(), base);
        return matches;
    }
}
