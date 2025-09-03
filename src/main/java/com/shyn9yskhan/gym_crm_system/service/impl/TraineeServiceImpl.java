package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.dto.*;
import com.shyn9yskhan.gym_crm_system.domain.Trainee;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.repository.TraineeRepository;
import com.shyn9yskhan.gym_crm_system.service.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public TraineeServiceImpl(TraineeRepository traineeRepository, UserService userService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @Override
    @Transactional
    public CreateTraineeResponse createTrainee(TraineeDto traineeDto) {
        logger.debug("Creating new trainee from DTO: {}", traineeDto);

        String firstname = traineeDto.getFirstname();
        String lastname = traineeDto.getLastname();
        LocalDate dateOfBirth = traineeDto.getDateOfBirth();
        String address = traineeDto.getAddress();

        UserCreationResult userCreationResult = userService.createUser(firstname, lastname);

        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setDateOfBirth(dateOfBirth);
        traineeEntity.setAddress(address);
        traineeEntity.setUser(userCreationResult.userEntity());

        traineeRepository.save(traineeEntity);

        logger.info("Created trainee with id: {} and username: {}", traineeEntity.getId(),
                traineeEntity.getUser() != null ? traineeEntity.getUser().getUsername() : "null");

        return new CreateTraineeResponse(
                traineeEntity.getUser() != null ? traineeEntity.getUser().getUsername() : null,
                traineeEntity.getUser() != null ? traineeEntity.getUser().getPassword() : null
        );
    }

    @Override
    @Transactional
    public TraineeProfile updateTrainee(UpdateTraineeRequest updateTraineeRequest) {
        String username = updateTraineeRequest.getUsername();
        LocalDate updatedDateOfBirth = updateTraineeRequest.getDateOfBirth();
        String updatedAddress = updateTraineeRequest.getAddress();

        Optional<TraineeEntity> optionalTraineeEntity = traineeRepository.findByUserUsername(username);
        if (optionalTraineeEntity.isEmpty()) {
            logger.warn("Trainee not found by username: {}", username);
            return null;
        }

        TraineeEntity traineeEntity = optionalTraineeEntity.get();
        String traineeId = traineeEntity.getId();

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setFirstname(updateTraineeRequest.getFirstname());
        userDto.setLastname(updateTraineeRequest.getLastname());
        userDto.setActive(updateTraineeRequest.isActive());

        int traineeUpdateCount = traineeRepository.updateTrainee(traineeId, updatedDateOfBirth, updatedAddress);
        UserDto updatedUser = userService.updateUser(userDto);

        if (traineeUpdateCount == 1 && updatedUser != null) {
            return getTraineeProfileById(traineeId);
        } else {
            logger.warn("Failed to update trainee (id: {}) or user (username: {}). traineeUpdateCount={}, userUpdated={}",
                    traineeId, username, traineeUpdateCount, updatedUser != null);
            return null;
        }
    }

    @Override
    @Transactional
    public String deleteTrainee(String username) {
        logger.debug("Deleting trainee with username: {}", username);

        boolean isExists = traineeRepository.existsByUserUsername(username);
        if (!isExists) {
            logger.warn("No trainee found for username: {}", username);
            return null;
        }

        long deleted = traineeRepository.deleteByUser_Username(username);
        String userDeleteResult = userService.deleteUser(username); // keep current behaviour

        if (deleted > 0 && userDeleteResult != null) {
            logger.info("Deleted trainee-row(s) for username: {} (rows={})", username, deleted);
            return username;
        } else {
            logger.warn("Failed to fully delete trainee or user for username: {} (rows={}, userDeleted={})",
                    username, deleted, userDeleteResult != null);
            return null;
        }
    }

    @Override
    public Trainee getTrainee(String traineeId) {
        logger.debug("Fetching trainee with ID: {}", traineeId);

        Optional<TraineeEntity> optionalTraineeEntity = traineeRepository.findById(traineeId);
        if (optionalTraineeEntity.isEmpty()) {
            logger.warn("Trainee not found with ID: {}", traineeId);
            return null;
        }

        TraineeEntity traineeEntity = optionalTraineeEntity.get();
        UserEntity userEntity = traineeEntity.getUser();

        Trainee trainee = new Trainee();
        trainee.setFirstname(userEntity.getFirstname());
        trainee.setLastname(userEntity.getLastname());
        trainee.setUsername(userEntity.getUsername());
        trainee.setPassword(userEntity.getPassword());
        trainee.setActive(userEntity.isActive());
        trainee.setDateOfBirth(traineeEntity.getDateOfBirth());
        trainee.setAddress(traineeEntity.getAddress());
        trainee.setUserId(userEntity.getId());
        return trainee;
    }

    @Override
    public TraineeEntity getTraineeEntity(String traineeId) {
        logger.debug("Fetching traineeEntity with ID: {}", traineeId);
        return traineeRepository.findById(traineeId).orElse(null);
    }

    @Override
    public TraineeEntity getTraineeEntityByUsername(String username) {
        Optional<TraineeEntity> optionalTraineeEntity = traineeRepository.findByUserUsername(username);
        return optionalTraineeEntity.orElse(null);
    }

    @Override
    public TraineeEntity saveTrainee(TraineeEntity traineeEntity) {
        return traineeRepository.save(traineeEntity);
    }

    @Override
    public TraineeProfile getTraineeProfileByUsername(String username) {
        return traineeRepository.findByUserUsername(username)
                .map(this::mapToTraineeProfile)
                .orElse(null);
    }

    @Override
    @Transactional
    public List<TrainerProfileDto> updateTraineesTrainerList(UpdateTraineesTrainerListRequest updateTraineesTrainerListRequest) {
        String traineeUsername = updateTraineesTrainerListRequest.getTraineeUsername();
        List<String> trainersUsernames = updateTraineesTrainerListRequest.getTrainers();

        Optional<TraineeEntity> optionalTraineeEntity = traineeRepository.findByUserUsername(traineeUsername);

        if (optionalTraineeEntity.isPresent()) {
            TraineeEntity traineeEntity = optionalTraineeEntity.get();
            Set<TrainerEntity> trainerEntities = new HashSet<>(trainerService.findAllByUserUsernameIn(trainersUsernames));
            traineeEntity.setTrainers(trainerEntities);
            traineeRepository.save(traineeEntity);

            List<TrainerProfileDto> updatedTrainerList = new ArrayList<>();
            for (TrainerEntity trainerEntity : trainerEntities) {
                TrainerProfileDto updatedTrainerProfileDto = new TrainerProfileDto();
                updatedTrainerProfileDto.setUsername(trainerEntity.getUser().getUsername());
                updatedTrainerProfileDto.setFirstname(trainerEntity.getUser().getFirstname());
                updatedTrainerProfileDto.setLastname(trainerEntity.getUser().getLastname());

                TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
                trainingTypeDto.setId(trainerEntity.getSpecialization().getId());
                trainingTypeDto.setTrainingTypeName(trainerEntity.getSpecialization().getTrainingTypeName());

                updatedTrainerProfileDto.setSpecialization(trainingTypeDto);

                updatedTrainerList.add(updatedTrainerProfileDto);
            }
            return updatedTrainerList;
        }
        return null;
    }

    @Override
    public List<GetTraineeTrainingsListResponse> getTraineeTrainingsList(GetTraineeTrainingsListRequest getTraineeTrainingsListRequest) {
        return trainingService.getTraineeTrainingsList(getTraineeTrainingsListRequest);
    }

    @Override
    public String updateTraineeActivation(String username, boolean isActive) {
        boolean isExists = traineeRepository.existsByUserUsername(username);
        if (isExists) return userService.setActiveByUsername(username, isActive);
        return null;
    }

    public TraineeProfile getTraineeProfileById(String id) {
        return traineeRepository.findById(id)
                .map(this::mapToTraineeProfile)
                .orElse(null);
    }

    private TraineeProfile mapToTraineeProfile(TraineeEntity traineeEntity) {
        UserEntity userEntity = traineeEntity.getUser();

        TraineeProfile traineeProfile = new TraineeProfile();
        traineeProfile.setUsername(userEntity != null ? userEntity.getUsername() : null);
        traineeProfile.setFirstname(userEntity != null ? userEntity.getFirstname() : null);
        traineeProfile.setLastname(userEntity != null ? userEntity.getLastname() : null);
        traineeProfile.setDateOfBirth(traineeEntity.getDateOfBirth());
        traineeProfile.setAddress(traineeEntity.getAddress());
        traineeProfile.setActive(userEntity != null && userEntity.isActive());

        Set<TrainerEntity> trainerEntities = traineeEntity.getTrainers();
        if (trainerEntities == null || trainerEntities.isEmpty()) {
            traineeProfile.setTrainers(Collections.emptyList());
            return traineeProfile;
        }

        List<TrainerProfileDto> trainers = new ArrayList<>(trainerEntities.size());
        for (TrainerEntity trainerEntity : trainerEntities) {
            TrainerProfileDto dto = new TrainerProfileDto();
            UserEntity user = trainerEntity.getUser();
            dto.setFirstname(user != null ? user.getFirstname() : null);
            dto.setLastname(user != null ? user.getLastname() : null);
            dto.setUsername(user != null ? user.getUsername() : null);

            if (trainerEntity.getSpecialization() != null) {
                TrainingTypeDto tt = new TrainingTypeDto();
                tt.setId(trainerEntity.getSpecialization().getId());
                tt.setTrainingTypeName(trainerEntity.getSpecialization().getTrainingTypeName());
                dto.setSpecialization(tt);
            }

            trainers.add(dto);
        }

        traineeProfile.setTrainers(trainers);
        return traineeProfile;
    }
}