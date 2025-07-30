package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.repository.TraineeRepository;
import com.shyn9yskhan.gym_crm_system.service.RandomGenerator;
import com.shyn9yskhan.gym_crm_system.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private TraineeRepository traineeRepository;

    public TraineeServiceImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Trainee createTrainee(TraineeDto traineeDto) {
        logger.debug("Creating new trainee from DTO: {}", traineeDto);
        String firstname = traineeDto.getFirstname();
        String lastname = traineeDto.getLastname();
        String base = firstname + "." + lastname;
        String username = makeUniqueUsername(base);

        String password = RandomGenerator.generatePassword();
        boolean isActive = true;
        LocalDate dateOfBirth = traineeDto.getDateOfBirth();
        String address = traineeDto.getAddress();
        String userId = RandomGenerator.generateUserId();

        Trainee trainee = new Trainee(firstname, lastname, username, password, isActive, dateOfBirth, address, userId);
        logger.info("Generated trainee with username: {} and userId: {}", username, userId);
        return traineeRepository.createTrainee(trainee);
    }

    @Override
    public Trainee updateTrainee(String traineeId, TraineeDto traineeDto) {
        logger.debug("Updating trainee with ID: {}", traineeId);
        Trainee existing = traineeRepository.getTrainee(traineeId);
        if (existing == null) {
            logger.warn("Trainee with ID: {} not found. Update aborted.", traineeId);
            return null;
        }

        String firstname = traineeDto.getFirstname();
        String lastname  = traineeDto.getLastname();
        existing.setFirstname(firstname);
        existing.setLastname(lastname);

        String base = firstname + "." + lastname;
        String newUsername = makeUniqueUsername(base);
        existing.setUsername(newUsername);

        existing.setDateOfBirth(traineeDto.getDateOfBirth());
        existing.setAddress(traineeDto.getAddress());

        Trainee updatedTrainee = traineeRepository.updateTrainee(traineeId, existing);
        if (updatedTrainee == null) {
            logger.warn("Failed to update trainee with ID: {}", traineeId);
            return null;
        }
        logger.info("Updated trainee: ID={}, new username={}", traineeId, newUsername);
        return updatedTrainee;
    }

    @Override
    public Trainee deleteTrainee(String traineeId) {
        logger.debug("Deleting trainee with ID: {}", traineeId);
        Trainee deletedTrainee = traineeRepository.deleteTrainee(traineeId);

        if (deletedTrainee == null) {
            logger.warn("Failed to delete trainee with ID: {}", traineeId);
            return null;
        } else {
            logger.info("Successfully deleted trainee with ID: {}", traineeId);
            return deletedTrainee;
        }
    }

    @Override
    public Trainee getTrainee(String traineeId) {
        logger.debug("Fetching trainee with ID: {}", traineeId);
        return traineeRepository.getTrainee(traineeId);
    }

    private String makeUniqueUsername(String base) {
        logger.debug("Generating unique username based on: {}", base);
        if (!traineeRepository.existsByUsername(base)) {
            return base;
        }

        List<String> similar = traineeRepository.findUsernamesByBase(base);
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
