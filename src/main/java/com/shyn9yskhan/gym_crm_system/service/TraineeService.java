package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dao.TraineeDAO;
import com.shyn9yskhan.gym_crm_system.dto.TraineeDTO;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    @Autowired
    private TraineeDAO traineeDAO;

    public Trainee createTrainee(TraineeDTO traineeDTO) {
        logger.debug("Creating new trainee from DTO: {}", traineeDTO);
        String firstname = traineeDTO.getFirstname();
        String lastname = traineeDTO.getLastname();
        String base = firstname + "." + lastname;
        String username = makeUniqueUsername(base);

        String password = RandomGenerator.generatePassword();
        boolean isActive = true;
        LocalDate dateOfBirth = traineeDTO.getDateOfBirth();
        String address = traineeDTO.getAddress();
        String userId = RandomGenerator.generateUserId();

        Trainee trainee = new Trainee(firstname, lastname, username, password, isActive, dateOfBirth, address, userId);
        logger.info("Generated trainee with username: {} and userId: {}", username, userId);
        return traineeDAO.createTrainee(trainee);
    }

    public boolean updateTrainee(String traineeId, TraineeDTO traineeDTO) {
        logger.debug("Updating trainee with ID: {}", traineeId);
        Trainee existing = traineeDAO.getTrainee(traineeId);
        if (existing == null) {
            logger.warn("Trainee with ID: {} not found. Update aborted.", traineeId);
            return false;
        }

        String firstname = traineeDTO.getFirstname();
        String lastname  = traineeDTO.getLastname();
        existing.setFirstname(firstname);
        existing.setLastname(lastname);

        String base = firstname + "." + lastname;
        String newUsername = makeUniqueUsername(base);
        existing.setUsername(newUsername);

        existing.setDateOfBirth(traineeDTO.getDateOfBirth());
        existing.setAddress(traineeDTO.getAddress());

        logger.info("Updated trainee: ID={}, new username={}", traineeId, newUsername);
        return traineeDAO.updateTrainee(traineeId, existing);
    }

    public boolean deleteTrainee(String traineeId) {
        logger.debug("Deleting trainee with ID: {}", traineeId);
        boolean success = traineeDAO.deleteTrainee(traineeId);

        if (success) {
            logger.info("Successfully deleted trainee with ID: {}", traineeId);
        } else {
            logger.warn("Failed to delete trainee with ID: {}", traineeId);
        }

        return success;
    }

    public Trainee getTrainee(String traineeId) {
        logger.debug("Fetching trainee with ID: {}", traineeId);
        return traineeDAO.getTrainee(traineeId);
    }


    private String makeUniqueUsername(String base) {
        logger.debug("Generating unique username based on: {}", base);
        if (!traineeDAO.existsByUsername(base)) {
            return base;
        }

        List<String> similar = traineeDAO.findUsernamesByBase(base);
        int max = similar.stream()
                .map(u -> u.substring(base.length()))
                .map(s -> {
                    if (s.isEmpty()) return 0;
                    try { return Integer.parseInt(s); }
                    catch (NumberFormatException ex) { return 0; }
                })
                .max(Integer::compareTo)
                .orElse(0);

        String uniqueUsername = base + (max + 1);
        logger.debug("Resolved unique username: {}", uniqueUsername);
        return uniqueUsername;
    }
}