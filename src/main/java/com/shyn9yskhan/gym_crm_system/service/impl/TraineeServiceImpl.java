package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.domain.Trainee;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.repository.TraineeRepository;
import com.shyn9yskhan.gym_crm_system.service.TraineeService;
import com.shyn9yskhan.gym_crm_system.service.UserCreationResult;
import com.shyn9yskhan.gym_crm_system.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private TraineeRepository traineeRepository;
    private UserService userService;

    public TraineeServiceImpl(TraineeRepository traineeRepository, UserService userService) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public String createTrainee(TraineeDto traineeDto) {
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
        logger.info("Created trainee with userId: {}", traineeEntity.getId());
        return traineeEntity.getId();
    }

    @Override
    public String updateTrainee(String traineeId, LocalDate updatedDateOfBirth, String updatedAddress) {
        logger.debug("Updating trainee with ID: {}", traineeId);
        boolean isExists = traineeRepository.existsById(traineeId);
        if (!isExists) {
            logger.warn("Trainee with ID: {} not found. Update aborted.", traineeId);
            return null;
        }
        int updated = traineeRepository.updateTrainee(traineeId, updatedDateOfBirth, updatedAddress);

        if (updated == 1) {
            logger.info("Updated trainee: ID={}", traineeId);
            return traineeId;
        } else {
            logger.warn("Failed to update trainee with ID: {}", traineeId);
            return null;
        }
    }

    @Override
    public String deleteTrainee(String traineeId) {
        logger.debug("Deleting trainee with ID: {}", traineeId);
        boolean isExists = traineeRepository.existsById(traineeId);
        if (!isExists) {
            logger.warn("Trainee with ID: {} not found. Delete aborted.", traineeId);
            return null;
        }
        traineeRepository.deleteById(traineeId);
        return traineeId;
    }

    @Override
    public Trainee getTrainee(String traineeId) {
        logger.debug("Fetching trainee with ID: {}", traineeId);

        Optional<TraineeEntity> optionalTraineeEntity = traineeRepository.findById(traineeId);

        if (optionalTraineeEntity.isEmpty()) {
            logger.warn("Trainee not found with ID: {}" , traineeId);
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
        Optional<TraineeEntity> optionalTraineeEntity = traineeRepository.findById(traineeId);
        if (optionalTraineeEntity.isEmpty()) {
            logger.warn("TraineeEntity not found with ID: {}" , traineeId);
            return null;
        }
        TraineeEntity traineeEntity = optionalTraineeEntity.get();
        UserEntity userEntity = traineeEntity.getUser();
        return traineeEntity;
    }

    @Override
    public TraineeEntity saveTrainee(TraineeEntity traineeEntity) {
        return traineeRepository.save(traineeEntity);
    }
}
