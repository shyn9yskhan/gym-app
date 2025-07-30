package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.model.Trainee;

public interface TraineeService {
    Trainee createTrainee(TraineeDto traineeDto);
    Trainee updateTrainee(String traineeId, TraineeDto traineeDTO);
    Trainee deleteTrainee(String traineeId);
    Trainee getTrainee(String traineeId);
}
