package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTrainingRepositoryTest {

    private InMemoryTrainingRepository dao;

    @BeforeEach
    void setUp() {
        Map<String, Training> store = new HashMap<>();
        dao = new InMemoryTrainingRepository(store);
    }

    @Test
    void createAndGetTraining() {
        Training t = new Training("TR1", "R1", "Workout",
                new TrainingType("CARDIO"),
                LocalDateTime.now(), Duration.ofHours(1));
        dao.createTraining(t);
        assertEquals(t, dao.getTraining("TR1"));
    }

    @Test
    void updateExistingTraining() {
        Training t = new Training("TR2", "R2", "Yoga",
                new TrainingType("YOGA"),
                LocalDateTime.now(), Duration.ofMinutes(45));
        dao.createTraining(t);
        t.setName("Power Yoga");
        Training updated = dao.updateTraining("TR2", t);
        assertNotNull(updated);
        assertEquals("Power Yoga", updated.getName());
    }

    @Test
    void updateNonExistingTraining() {
        Training result = dao.updateTraining("NOPE", null);
        assertNull(result);
    }

    @Test
    void deleteTraining() {
        Training t = new Training("TR3", "R3", "Pilates",
                new TrainingType("PILATES"),
                LocalDateTime.now(), Duration.ofHours(1));
        dao.createTraining(t);
        Training removed = dao.deleteTraining("TR3");
        assertEquals(t, removed);
        assertNull(dao.getTraining("TR3"));
    }

    @Test
    void deleteNonExistingTraining() {
        Training removed = dao.deleteTraining("NOPE");
        assertNull(removed);
    }
}