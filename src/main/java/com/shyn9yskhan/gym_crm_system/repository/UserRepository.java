package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUsername(String username);
    List<String> findUsernameByUsernameStartingWith(String base);
    Optional<UserEntity> findByUsername(String username);
    long deleteByUsername(String username);
}
