package com.shyn9yskhan.gym_crm_system;

import com.shyn9yskhan.gym_crm_system.repository.TraineeRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TraineeDbHealthIndicator implements HealthIndicator {
    private final TraineeRepository traineeRepository;

    public TraineeDbHealthIndicator(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Health health() {
        try {
            long count = traineeRepository.count();
            return Health.up()
                    .withDetail("traineeCount", count)
                    .build();
        } catch (Exception ex) {
            return Health.down(ex).withDetail("error", ex.getMessage()).build();
        }
    }
}
