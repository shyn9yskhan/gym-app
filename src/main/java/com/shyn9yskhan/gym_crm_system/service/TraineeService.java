package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.*;
import com.shyn9yskhan.gym_crm_system.domain.Trainee;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;

import java.util.List;

public interface TraineeService {
    CreateTraineeResponse createTrainee(TraineeDto traineeDto);
    TraineeProfile updateTrainee(UpdateTraineeRequest updateTraineeRequest);
    String deleteTrainee(String traineeId);
    Trainee getTrainee(String traineeId);
    TraineeEntity getTraineeEntity(String traineeId);
    TraineeEntity getTraineeEntityByUsername(String username);
    TraineeEntity saveTrainee(TraineeEntity traineeEntity);
    TraineeProfile getTraineeProfileByUsername(String username);
    List<TrainerProfileDto> updateTraineesTrainerList(UpdateTraineesTrainerListRequest updateTraineesTrainerListRequest);
    List<GetTraineeTrainingsListResponse> getTraineeTrainingsList(GetTraineeTrainingsListRequest traineeTrainingsListRequest);
    String updateTraineeActivation(String username, boolean isActive);
}
