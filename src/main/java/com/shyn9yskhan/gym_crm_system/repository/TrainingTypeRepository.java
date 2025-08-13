package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingTypeEntity, String> {
    Optional<TrainingTypeEntity> findByTrainingTypeName(String trainingTypeName);
}
