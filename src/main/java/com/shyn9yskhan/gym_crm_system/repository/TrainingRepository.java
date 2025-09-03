package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<TrainingEntity, String> {
    @Query("""
        SELECT DISTINCT t
        FROM TrainingEntity t
        JOIN t.trainee tra
        JOIN tra.user traineeUser
        JOIN t.trainer tr
        JOIN tr.user trainerUser
        JOIN t.trainingType tt
        WHERE (:traineeUsername IS NULL OR traineeUser.username = :traineeUsername)
          AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
          AND (:toDate   IS NULL OR t.trainingDate <= :toDate)
          AND (:trainerUsername IS NULL OR trainerUser.username = :trainerUsername)
          AND (:trainingTypeName IS NULL OR tt.trainingTypeName = :trainingTypeName)
    """)
    List<TrainingEntity> findTraineeTrainings(
            @Param("traineeUsername") String traineeUsername,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("trainerUsername") String trainerUsername,
            @Param("trainingTypeName") String trainingTypeName
    );

    @Query("""
        SELECT DISTINCT t
        FROM TrainingEntity t
        JOIN t.trainer tr
        JOIN tr.user trainerUser
        JOIN t.trainee tra
        JOIN tra.user traineeUser
        JOIN t.trainingType tt
        WHERE (:trainerUsername IS NULL OR trainerUser.username = :trainerUsername)
          AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
          AND (:toDate   IS NULL OR t.trainingDate <= :toDate)
          AND (:traineeUsername IS NULL OR traineeUser.username = :traineeUsername)
    """)
    List<TrainingEntity> findTrainerTrainings(
            @Param("trainerUsername") String trainerUsername,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("traineeUsername") String traineeUsername
    );
}
