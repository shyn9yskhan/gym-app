package com.shyn9yskhan.gym_crm_system;

import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StorageConfig {

    @Bean(name = "traineeStorage")
    public Map<String, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean(name = "trainerStorage")
    public Map<String, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean(name = "trainingStorage")
    public Map<String, Training> trainingStorage() {
        return new HashMap<>();
    }
}