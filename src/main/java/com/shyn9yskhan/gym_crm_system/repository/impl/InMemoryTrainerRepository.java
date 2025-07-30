package com.shyn9yskhan.gym_crm_system.repository.impl;

import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.repository.TrainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryTrainerRepository implements TrainerRepository {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryTrainerRepository.class);
    private final Map<String, Trainer> trainers;

    public InMemoryTrainerRepository(@Qualifier("trainerStorage") Map<String, Trainer> trainers) {
        this.trainers = trainers;
        logger.debug("TrainerDAO initialized with trainer storage.");
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        trainers.put(trainer.getUserId(), trainer);
        logger.info("Created trainer with ID: {}", trainer.getUserId());
        return trainer;
    }

    @Override
    public Trainer updateTrainer(String trainerId, Trainer updatedTrainer) {
        logger.debug("Attempting to update trainer with ID: {}", trainerId);
        if (!trainers.containsKey(trainerId)) {
            logger.warn("Trainer with ID: {} not found. Update failed.", trainerId);
            return null;
        }
        trainers.put(trainerId, updatedTrainer);
        logger.info("Updated trainer with ID: {}", trainerId);
        return updatedTrainer;
    }

    @Override
    public Trainer deleteTrainer(String trainerId) {
        Trainer removedTrainer = trainers.remove(trainerId);
        if (removedTrainer != null) {
            logger.info("Deleted trainer with ID: {}", trainerId);
        } else {
            logger.warn("Trainer with ID: {} not found. Delete failed.", trainerId);
        }
        return removedTrainer;
    }

    @Override
    public Trainer getTrainer(String trainerId) {
        logger.debug("Retrieving trainer with ID: {}", trainerId);
        return trainers.get(trainerId);
    }

    @Override
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

    @Override
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
