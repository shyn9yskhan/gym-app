package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrainerRepository extends JpaRepository<TrainerEntity, String> {
    @Modifying
    @Query("UPDATE TrainerEntity t SET t.specialization = :specialization WHERE t.id = :trainerId")
    int updateTrainerSpecialization(@Param("trainerId") String trainerId, @Param("specialization") TrainingTypeEntity specialization);
}
