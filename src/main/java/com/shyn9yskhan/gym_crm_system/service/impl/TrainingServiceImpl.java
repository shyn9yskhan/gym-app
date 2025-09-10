package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.domain.TrainingType;
import com.shyn9yskhan.gym_crm_system.dto.*;
import com.shyn9yskhan.gym_crm_system.domain.Training;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainingRepository;
import com.shyn9yskhan.gym_crm_system.service.TraineeService;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
import com.shyn9yskhan.gym_crm_system.service.TrainingService;
import com.shyn9yskhan.gym_crm_system.service.TrainingTypeService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private TrainingRepository trainingRepository;
    TraineeService traineeService;
    TrainerService trainerService;
    TrainingTypeService trainingTypeService;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeService traineeService, TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    @Transactional
    public String addTraining(AddTrainingRequest addTrainingRequest) {
        logger.info("Creating new training: {}", addTrainingRequest.getTrainingName());

        String traineeUsername = addTrainingRequest.getTraineeUsername();
        String trainerUsername = addTrainingRequest.getTrainerUsername();
        String trainingName = addTrainingRequest.getTrainingName();
        LocalDate trainingDate = addTrainingRequest.getTrainingDate();
        int trainingDuration = addTrainingRequest.getTrainingDuration();

        TraineeEntity traineeEntity = traineeService.getTraineeEntityByUsername(traineeUsername);
        TrainerEntity trainerEntity = trainerService.getTrainerEntityByUsername(trainerUsername);
        TrainingTypeEntity trainingTypeEntity = (trainerEntity != null) ? trainerEntity.getSpecialization() : null;

        if (traineeEntity != null && trainerEntity != null && trainingTypeEntity != null) {
            TrainingEntity trainingEntity = new TrainingEntity();

            trainingEntity.setTrainee(traineeEntity);
            trainingEntity.setTrainer(trainerEntity);
            trainingEntity.setTrainingName(trainingName);
            trainingEntity.setTrainingType(trainingTypeEntity);
            trainingEntity.setTrainingDate(trainingDate);
            trainingEntity.setTrainingDuration(trainingDuration);

            traineeEntity.addTrainer(trainerEntity);
            trainerEntity.addTrainee(traineeEntity);

            trainingRepository.save(trainingEntity);
            traineeService.saveTrainee(traineeEntity);
            trainerService.saveTrainer(trainerEntity);

            logger.info("Training created with ID: {}", trainingEntity.getId());
            return trainingEntity.getId();
        }
        return null;
    }

    @Override
    public Training getTraining(String trainingId) {
        logger.info("Fetching training with ID: {}", trainingId);
        Optional<TrainingEntity> optionalTrainingEntity = trainingRepository.findById(trainingId);
        if (optionalTrainingEntity.isEmpty()) {
            logger.warn("Training with ID {} not found", trainingId);
            return null;
        }
        TrainingEntity trainingEntity = optionalTrainingEntity.get();
        TraineeEntity traineeEntity = trainingEntity.getTrainee();
        TrainerEntity trainerEntity = trainingEntity.getTrainer();
        String trainingName = trainingEntity.getTrainingName();
        TrainingTypeEntity trainingTypeEntity = trainingEntity.getTrainingType();
        LocalDate trainingDate = trainingEntity.getTrainingDate();
        int trainingDuration = trainingEntity.getTrainingDuration();

        Training training = new Training();
        training.setTraineeId(traineeEntity.getId());
        training.setTrainerId(trainerEntity.getId());
        training.setName(trainingName);
        training.setType(new TrainingType(trainingTypeEntity.getTrainingTypeName()));
        training.setDate(trainingDate);
        training.setDuration(trainingDuration);

        return training;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<GetTraineeTrainingsListResponse> getTraineeTrainingsList(GetTraineeTrainingsListRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            logger.warn("Trainee username is required for getTraineeTrainingsList");
            return new ArrayList<>();
        }

        String traineeUsername = request.getUsername();
        LocalDate fromDate = request.getPeriodFrom();
        LocalDate toDate = request.getPeriodTo();
        String trainerUsername = request.getTrainerUsername();
        String trainingTypeName = request.getTrainingType() != null ? request.getTrainingType().getTrainingTypeName() : null;

        List<TrainingEntity> trainings = trainingRepository.findTraineeTrainings(
                traineeUsername, fromDate, toDate, trainerUsername, trainingTypeName
        );

        List<GetTraineeTrainingsListResponse> responses = new ArrayList<>(trainings.size());
        for (TrainingEntity trainingEntity : trainings) {
            responses.add(mapTrainingEntityToTraineeResponse(trainingEntity));
        }
        return responses;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<GetTrainerTrainingsListResponse> getTrainerTrainingsList(GetTrainerTrainingsListRequest request) {
        if (request == null || request.getTrainerUsername() == null || request.getTrainerUsername().isBlank()) {
            logger.warn("Trainer username is required for getTrainerTrainingsList");
            return new ArrayList<>();
        }

        String trainerUsername = request.getTrainerUsername();
        LocalDate fromDate = request.getPeriodFrom();
        LocalDate toDate = request.getPeriodTo();
        String traineeUsername = request.getTraineeUsername();

        List<TrainingEntity> trainings = trainingRepository.findTrainerTrainings(
                trainerUsername, fromDate, toDate, traineeUsername
        );

        List<GetTrainerTrainingsListResponse> responses = new ArrayList<>(trainings.size());
        for (TrainingEntity trainingEntity : trainings) {
            responses.add(mapTrainingEntityToTrainerResponse(trainingEntity));
        }
        return responses;
    }

    private GetTraineeTrainingsListResponse mapTrainingEntityToTraineeResponse(TrainingEntity entity) {
        GetTraineeTrainingsListResponse dto = new GetTraineeTrainingsListResponse();

        TrainerEntity trainerEntity = entity.getTrainer();
        if (trainerEntity != null && trainerEntity.getUser() != null) {
            dto.setTrainerUsername(trainerEntity.getUser().getUsername());
        }

        dto.setTrainingDate(entity.getTrainingDate());
        dto.setTrainingDuration(entity.getTrainingDuration());
        dto.setTrainingName(entity.getTrainingName());

        TrainingTypeEntity tt = entity.getTrainingType();
        if (tt != null) {
            dto.setTrainingType(new TrainingTypeDto(tt.getId(), tt.getTrainingTypeName()));
        }

        return dto;
    }

    private GetTrainerTrainingsListResponse mapTrainingEntityToTrainerResponse(TrainingEntity entity) {
        GetTrainerTrainingsListResponse dto = new GetTrainerTrainingsListResponse();

        dto.setTrainingName(entity.getTrainingName());
        dto.setTrainingDate(entity.getTrainingDate());
        dto.setTrainingDuration(entity.getTrainingDuration());

        TrainingTypeEntity tt = entity.getTrainingType();
        if (tt != null) {
            dto.setTrainingType(new TrainingTypeDto(tt.getId(), tt.getTrainingTypeName()));
        }

        TraineeEntity traineeEntity = entity.getTrainee();
        if (traineeEntity != null && traineeEntity.getUser() != null) {
            dto.setTraineeUsername(traineeEntity.getUser().getUsername());
        }

        return dto;
    }
}
