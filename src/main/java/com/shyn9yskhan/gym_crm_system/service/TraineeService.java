package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.domain.Trainee;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;

import java.time.LocalDate;

public interface TraineeService {
    String createTrainee(TraineeDto traineeDto);
    String updateTrainee(String traineeId, LocalDate updatedDateOfBirth, String updatedAddress);
    String deleteTrainee(String traineeId);
    Trainee getTrainee(String traineeId);
    TraineeEntity getTraineeEntity(String traineeId);
    TraineeEntity saveTrainee(TraineeEntity traineeEntity);
}
