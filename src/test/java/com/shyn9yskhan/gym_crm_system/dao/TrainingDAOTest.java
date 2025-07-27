package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDAOTest {

    private TrainingDAO dao;

    @BeforeEach
    void setUp() {
        Map<String, Training> store = new HashMap<>();
        dao = new TrainingDAO(store);
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
        t.setTrainingName("Power Yoga");
        assertTrue(dao.updateTraining("TR2", t));
        assertEquals("Power Yoga", dao.getTraining("TR2").getTrainingName());
    }

    @Test
    void updateNonExistingTraining() {
        assertFalse(dao.updateTraining("NOPE", null));
    }

    @Test
    void deleteTraining() {
        Training t = new Training("TR3", "R3", "Pilates",
                new TrainingType("PILATES"),
                LocalDateTime.now(), Duration.ofHours(1));
        dao.createTraining(t);
        assertTrue(dao.deleteTraining("TR3"));
        assertNull(dao.getTraining("TR3"));
    }

    @Test
    void deleteNonExistingTraining() {
        assertFalse(dao.deleteTraining("NOPE"));
    }
}