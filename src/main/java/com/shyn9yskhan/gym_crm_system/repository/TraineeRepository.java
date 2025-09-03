package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<TraineeEntity, String> {
    @Modifying
    @Query("UPDATE TraineeEntity t SET t.dateOfBirth = :dateOfBirth, t.address = :address WHERE t.id = :traineeId")
    int updateTrainee(@Param("traineeId") String traineeId, @Param("dateOfBirth") LocalDate dateOfBirth, @Param("address") String address);
    Optional<TraineeEntity> findByUserUsername(String username);
    long deleteByUser_Username(String username);
    boolean existsByUserUsername(String username);
}
