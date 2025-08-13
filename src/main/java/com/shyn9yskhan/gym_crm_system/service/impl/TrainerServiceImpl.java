package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.domain.TrainingType;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.domain.Trainer;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainerRepository;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
import com.shyn9yskhan.gym_crm_system.service.TrainingTypeService;
import com.shyn9yskhan.gym_crm_system.service.UserCreationResult;
import com.shyn9yskhan.gym_crm_system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private TrainerRepository trainerRepository;
    private UserService userService;
    private TrainingTypeService trainingTypeService;

    public TrainerServiceImpl(TrainerRepository trainerRepository, UserService userService, TrainingTypeService trainingTypeService) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    public String createTrainer(TrainerDto trainerDto) {
        logger.info("Creating trainer: {} {}", trainerDto.getFirstname(), trainerDto.getLastname());

        String firstname = trainerDto.getFirstname();
        String lastname = trainerDto.getLastname();
        String trainingTypeName = trainerDto.getTrainingTypeName();

        UserCreationResult userCreationResult = userService.createUser(firstname, lastname);

        TrainerEntity trainerEntity = new TrainerEntity();

        TrainingTypeEntity trainingTypeEntity = trainingTypeService.getTrainingTypeByName(trainingTypeName);
        if (trainingTypeEntity == null) {
            return null;
        }

        trainerEntity.setSpecialization(trainingTypeEntity);
        trainerEntity.setUser(userCreationResult.userEntity());

        trainerRepository.save(trainerEntity);
        logger.info("Trainer created with userId={}", trainerEntity.getId());
        return trainerEntity.getId();
    }

    @Override
    public String updateTrainer(String trainerId, String updatedTrainingTypeName) {
        logger.info("Updating trainer with ID: {}", trainerId);

        boolean isExists = trainerRepository.existsById(trainerId);
        if (!isExists) {
            logger.warn("Trainee with ID: {} not found. Update aborted.", trainerId);
            return null;
        }

        TrainingTypeEntity trainingTypeEntity = trainingTypeService.getTrainingTypeByName(updatedTrainingTypeName);
        if (trainingTypeEntity == null) {
            return null;
        }

        int updated = trainerRepository.updateTrainerSpecialization(trainerId, trainingTypeEntity);

        if (updated == 1) {
            logger.info("Trainer updated successfully: {}", trainerId);
            return trainerId;
        } else {
            logger.warn("Failed to update trainer: {}", trainerId);
            return null;
        }
    }

    @Override
    public Trainer getTrainer(String trainerId) {
        logger.info("Retrieving trainer with ID: {}", trainerId);

        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository.findById(trainerId);

        if (optionalTrainerEntity.isEmpty()) {
            logger.warn("Trainer with ID: {} not found.", trainerId);
            return null;
        }

        TrainerEntity trainerEntity = optionalTrainerEntity.get();
        UserEntity userEntity = trainerEntity.getUser();

        Trainer trainer = new Trainer();
        trainer.setFirstname(userEntity.getFirstname());
        trainer.setLastname(userEntity.getLastname());
        trainer.setUsername(userEntity.getUsername());
        trainer.setPassword(userEntity.getPassword());
        trainer.setActive(userEntity.isActive());
        trainer.setSpecialization(new TrainingType(trainerEntity.getSpecialization().getTrainingTypeName()));
        return trainer;
    }

    @Override
    public TrainerEntity getTrainerEntity(String trainerId) {
        logger.info("Retrieving trainerEntity with ID: {}", trainerId);
        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository.findById(trainerId);
        if (optionalTrainerEntity.isEmpty()) {
            logger.warn("TrainerEntity with ID: {} not found.", trainerId);
            return null;
        }
        TrainerEntity trainerEntity = optionalTrainerEntity.get();
        UserEntity userEntity = trainerEntity.getUser();
        return trainerEntity;
    }

    @Override
    public TrainerEntity saveTrainer(TrainerEntity trainerEntity) {
        return trainerRepository.save(trainerEntity);
    }
}
