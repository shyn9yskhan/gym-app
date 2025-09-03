package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<TrainerEntity, String> {
    @Modifying
    @Query("UPDATE TrainerEntity t SET t.specialization = :specialization WHERE t.id = :trainerId")
    int updateTrainer(@Param("trainerId") String trainerId, @Param("specialization") TrainingTypeEntity specialization);
    Optional<TrainerEntity> findByUserUsername(String username);
    long deleteByUser_Username(String username);
    boolean existsByUserUsername(String username);
    @Query("""
        SELECT t FROM TrainerEntity t
        WHERE t.user.isActive = true
        AND NOT EXISTS (
            SELECT 1 FROM TraineeEntity tr
            JOIN tr.trainers trainer
            WHERE tr.id = :traineeId
            AND trainer.id = t.id
        )
    """)
    List<TrainerEntity> findActiveTrainersNotAssignedToTrainee(@Param("traineeId") String traineeId);
    List<TrainerEntity> findAllByUserUsernameIn(List<String> usernames);
}
