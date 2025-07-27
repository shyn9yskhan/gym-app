package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TraineeDAO {
    private static final Logger log = LoggerFactory.getLogger(TraineeDAO.class);
    private final Map<String, Trainee> trainees;

    public TraineeDAO(@Qualifier("traineeStorage") Map<String, Trainee> trainees) {
        this.trainees = trainees;
        log.info("TraineeDAO initialized with storage capacity {}", trainees.size());
    }

    public Trainee createTrainee(Trainee trainee) {
        log.debug("Creating trainee with ID {} and username {}", trainee.getUserId(), trainee.getUsername());
        trainees.put(trainee.getUserId(), trainee);
        log.info("Trainee created: {}", trainee.getUserId());
        return trainee;
    }

    public boolean updateTrainee(String traineeId, Trainee updatedTrainee) {
        log.debug("Updating trainee {}", traineeId);
        if (!trainees.containsKey(traineeId)) {
            log.warn("Trainee {} not found for update", traineeId);
            return false;
        }
        trainees.put(traineeId, updatedTrainee);
        log.info("Trainee {} updated successfully", traineeId);
        return true;
    }

    public boolean deleteTrainee(String traineeId) {
        log.debug("Deleting trainee {}", traineeId);
        boolean removed = trainees.remove(traineeId) != null;
        if (removed) {
            log.info("Trainee {} deleted", traineeId);
        } else {
            log.warn("Trainee {} not found for deletion", traineeId);
        }
        return removed;
    }

    public Trainee getTrainee(String traineeId) {
        log.debug("Retrieving trainee {}", traineeId);
        Trainee t = trainees.get(traineeId);
        if (t == null) {
            log.warn("Trainee {} not found", traineeId);
        } else {
            log.info("Trainee {} retrieved", traineeId);
        }
        return t;
    }

    public boolean existsByUsername(String username) {
        log.debug("Checking existence of username {}", username);
        for (Trainee trainee : trainees.values()) {
            if (Objects.equals(trainee.getUsername(), username)) {
                log.debug("Username {} exists", username);
                return true;
            }
        }
        log.debug("Username {} does not exist", username);
        return false;
    }

    public List<String> findUsernamesByBase(String base) {
        log.debug("Finding usernames starting with '{}'");
        List<String> matches = new ArrayList<>();
        for (Trainee trainee : trainees.values()) {
            String username = trainee.getUsername();
            if (username != null && username.startsWith(base)) {
                matches.add(username);
            }
        }
        log.info("Found {} usernames with base {}", matches.size(), base);
        return matches;
    }
}