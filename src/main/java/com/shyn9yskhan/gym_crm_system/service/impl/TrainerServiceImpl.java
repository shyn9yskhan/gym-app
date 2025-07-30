package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.repository.TrainerRepository;
import com.shyn9yskhan.gym_crm_system.service.RandomGenerator;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public Trainer createTrainer(TrainerDto trainerDto) {
        logger.info("Creating trainer: {} {}", trainerDto.getFirstname(), trainerDto.getLastname());
        String firstname = trainerDto.getFirstname();
        String lastname = trainerDto.getLastname();
        String base = firstname + "." + lastname;
        String username = makeUniqueUsername(base);

        String password = RandomGenerator.generatePassword();
        boolean isActive = true;
        String trainingTypeName = trainerDto.getTrainingTypeName();
        TrainingType trainingType = new TrainingType(trainingTypeName);
        String userId = RandomGenerator.generateUserId();

        Trainer trainer = new Trainer(firstname, lastname, username, password, isActive, trainingType, userId);
        Trainer created = trainerRepository.createTrainer(trainer);
        logger.info("Trainer created with userId={}, username={}", created.getUserId(), created.getUsername());
        return created;
    }

    public Trainer updateTrainer(String trainerId, TrainerDto trainerDTO) {
        logger.info("Updating trainer with ID: {}", trainerId);
        Trainer existing = trainerRepository.getTrainer(trainerId);
        if (existing == null) {
            logger.warn("Trainer not found for ID: {}", trainerId);
            return null;
        }

        String firstname = trainerDTO.getFirstname();
        String lastname = trainerDTO.getLastname();
        String trainingTypeName = trainerDTO.getTrainingTypeName();
        TrainingType trainingType = new TrainingType(trainingTypeName);
        String base = firstname + "." + lastname;
        String username = makeUniqueUsername(base);

        existing.setFirstname(firstname);
        existing.setLastname(lastname);
        existing.setUsername(username);
        existing.setSpecialization(trainingType);

        Trainer updatedTrainer = trainerRepository.updateTrainer(trainerId, existing);
        if (updatedTrainer == null) {
            logger.warn("Failed to update trainer: {}", trainerId);
            return null;
        } else {
            logger.info("Trainer updated successfully: {}", trainerId);
            return updatedTrainer;
        }
    }

    public Trainer getTrainer(String trainerId) {
        logger.info("Retrieving trainer with ID: {}", trainerId);
        return trainerRepository.getTrainer(trainerId);
    }

    private String makeUniqueUsername(String base) {
        if (!trainerRepository.existsByUsername(base)) {
            return base;
        }

        List<String> similar = trainerRepository.findUsernamesByBase(base);
        int max = similar.stream()
                .map(u -> u.substring(base.length()))
                .map(s -> {
                    if (s.isEmpty()) return 0;
                    try { return Integer.parseInt(s); }
                    catch (NumberFormatException ex) { return 0; }
                })
                .max(Integer::compareTo)
                .orElse(0);

        return base + (max + 1);
    }
}
