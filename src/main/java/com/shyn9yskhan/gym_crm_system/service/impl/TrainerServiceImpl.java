package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.domain.TrainingType;
import com.shyn9yskhan.gym_crm_system.dto.*;
import com.shyn9yskhan.gym_crm_system.domain.Trainer;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainerRepository;
import com.shyn9yskhan.gym_crm_system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private TrainerRepository trainerRepository;
    private UserService userService;
    private TraineeService traineeService;
    private TrainingTypeService trainingTypeService;
    private TrainingService trainingService;

    public TrainerServiceImpl(TrainerRepository trainerRepository, UserService userService, TraineeService traineeService, TrainingTypeService trainingTypeService, TrainingService trainingService) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.traineeService = traineeService;
        this.trainingTypeService = trainingTypeService;
        this.trainingService = trainingService;
    }

    @Override
    public CreateTrainerResponse createTrainer(TrainerDto trainerDto) {
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
        return new CreateTrainerResponse(trainerEntity.getUser().getUsername(), trainerEntity.getUser().getPassword());
    }

    @Override
    public TrainerProfile updateTrainer(UpdateTrainerRequest updateTrainerRequest) {
        String username = updateTrainerRequest.getUsername();
        TrainingTypeDto updatedSpecialization = updateTrainerRequest.getSpecialization();

        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository.findByUserUsername(username);
        if (optionalTrainerEntity.isEmpty()) {
            return null;
        }

        TrainerEntity trainerEntity = optionalTrainerEntity.get();
        String trainerId = trainerEntity.getId();

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setFirstname(updateTrainerRequest.getFirstname());
        userDto.setLastname(updateTrainerRequest.getLastname());
        userDto.setActive(updateTrainerRequest.isActive());

        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId(updatedSpecialization.getId());
        trainingTypeEntity.setTrainingTypeName(updatedSpecialization.getTrainingTypeName());

        int trainerUpdateCount = trainerRepository.updateTrainer(trainerId, trainingTypeEntity);
        UserDto updatedUser = userService.updateUser(userDto);

        if (trainerUpdateCount == 1 && updatedUser != null) {
            return getTrainerProfileById(trainerId);
        } else {
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
    public TrainerEntity getTrainerEntityByUsername(String username) {
        Optional<TrainerEntity> optionalTrainerEntity = trainerRepository.findByUserUsername(username);
        if (optionalTrainerEntity.isPresent()) {
            TrainerEntity trainerEntity = optionalTrainerEntity.get();
            UserEntity userEntity = trainerEntity.getUser();
            return trainerEntity;
        }
        else return null;
    }

    @Override
    public TrainerEntity saveTrainer(TrainerEntity trainerEntity) {
        return trainerRepository.save(trainerEntity);
    }

    @Override
    public TrainerProfile getTrainerProfileByUsername(String username) {
        return trainerRepository.findByUserUsername(username)
                .map(this::mapToTrainerProfile)
                .orElse(null);
    }

    @Override
    public List<TrainerProfileDto> getNotAssignedOnTraineeActiveTrainers(String username) {
        List<TrainerProfileDto> trainers = new ArrayList<>();
        TraineeEntity traineeEntity = traineeService.getTraineeEntityByUsername(username);
        if (traineeEntity != null) {
            String traineeId = traineeEntity.getId();
            List<TrainerEntity> trainerEntities = trainerRepository.findActiveTrainersNotAssignedToTrainee(traineeId);
            for (TrainerEntity trainerEntity : trainerEntities) {
                TrainerProfileDto trainerProfileDto = new TrainerProfileDto();
                trainerProfileDto.setUsername(trainerEntity.getUser().getUsername());
                trainerProfileDto.setFirstname(trainerEntity.getUser().getFirstname());
                trainerProfileDto.setLastname(trainerEntity.getUser().getLastname());

                TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
                trainingTypeDto.setId(trainerEntity.getSpecialization().getId());
                trainingTypeDto.setTrainingTypeName(trainerEntity.getSpecialization().getTrainingTypeName());

                trainerProfileDto.setSpecialization(trainingTypeDto);
                trainers.add(trainerProfileDto);
            }
        }
        return trainers;
    }

    @Override
    public List<TrainerEntity> findAllByUserUsernameIn(List<String> usernames) {
        return trainerRepository.findAllByUserUsernameIn(usernames);
    }

    @Override
    public List<GetTrainerTrainingsListResponse> getTrainerTrainings(GetTrainerTrainingsListRequest request) {
        return trainingService.getTrainerTrainingsList(request);
    }

    @Override
    public String updateTrainerActivation(String username, boolean isActive) {
        boolean isExists = trainerRepository.existsByUserUsername(username);
        if (isExists) return userService.setActiveByUsername(username, isActive);
        else return null;
    }

    public TrainerProfile getTrainerProfileById(String id) {
        return trainerRepository.findById(id)
                .map(this::mapToTrainerProfile)
                .orElse(null);
    }

    private TrainerProfile mapToTrainerProfile(TrainerEntity trainerEntity) {
        UserEntity userEntity = trainerEntity.getUser();

        TrainerProfile trainerProfile = new TrainerProfile();
        trainerProfile.setUsername(userEntity != null ? userEntity.getUsername() : null);
        trainerProfile.setFirstname(userEntity != null ? userEntity.getFirstname() : null);
        trainerProfile.setLastname(userEntity != null ? userEntity.getLastname() : null);
        trainerProfile.setActive(userEntity != null && userEntity.isActive());

        if (trainerEntity.getSpecialization() != null) {
            TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
            trainingTypeDto.setId(trainerEntity.getSpecialization().getId());
            trainingTypeDto.setTrainingTypeName(trainerEntity.getSpecialization().getTrainingTypeName());
            trainerProfile.setSpecialization(trainingTypeDto);
        }

        Set<TraineeEntity> traineeEntities = trainerEntity.getTrainees();
        if (traineeEntities == null || traineeEntities.isEmpty()) {
            trainerProfile.setTrainees(Collections.emptyList());
            return trainerProfile;
        }

        List<TraineeProfileDto> trainees = new ArrayList<>(traineeEntities.size());
        for (TraineeEntity traineeEntity : traineeEntities) {
            TraineeProfileDto dto = new TraineeProfileDto();
            UserEntity user = traineeEntity.getUser();
            dto.setUsername(user != null ? user.getUsername() : null);
            dto.setFirstname(user != null ? user.getFirstname() : null);
            dto.setLastname(user != null ? user.getLastname() : null);

            trainees.add(dto);
        }
        trainerProfile.setTrainees(trainees);
        return trainerProfile;
    }
}
