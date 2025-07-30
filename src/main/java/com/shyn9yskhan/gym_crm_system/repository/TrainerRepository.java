package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.model.Trainer;

import java.util.List;

public interface TrainerRepository {
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(String trainerId, Trainer updatedTrainer);
    Trainer deleteTrainer(String trainerId);
    Trainer getTrainer(String trainerId);
    boolean existsByUsername(String username);
    List<String> findUsernamesByBase(String base);
}
