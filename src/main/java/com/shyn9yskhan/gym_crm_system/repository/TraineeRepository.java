package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.model.Trainee;

import java.util.List;

public interface TraineeRepository {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(String traineeId, Trainee updatedTrainee);
    Trainee deleteTrainee(String traineeId);
    Trainee getTrainee(String traineeId);
    boolean existsByUsername(String username);
    List<String> findUsernamesByBase(String base);
}
