package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTrainerRepositoryTest {

    private InMemoryTrainerRepository dao;

    @BeforeEach
    void setUp() {
        Map<String, Trainer> store = new HashMap<>();
        dao = new InMemoryTrainerRepository(store);
    }

    @Test
    void createAndGetTrainer() {
        Trainer t = new Trainer("Janex", "Does", "Janex.Does", "pw", true,
                new TrainingType("CARDIO"), "R145");
        dao.createTrainer(t);
        assertEquals(t, dao.getTrainer("R145"));
    }

    @Test
    void updateExistingTrainer() {
        Trainer t = new Trainer("Sam", "Lee", "Sam.Lee", "pw", true,
                new TrainingType("STRENGTH"), "R2");
        dao.createTrainer(t);
        t.setLastname("Li");
        Trainer updated = dao.updateTrainer("R2", t);
        assertNotNull(updated);
        assertEquals("Li", updated.getLastname());
    }

    @Test
    void updateNonExistingTrainer() {
        Trainer result = dao.updateTrainer("NOPE", null);
        assertNull(result);
    }

    @Test
    void deleteExistingTrainer() {
        Trainer t = new Trainer("A","B","A.B","pw",true,
                new TrainingType("YOGA"),"R3");
        dao.createTrainer(t);
        Trainer removed = dao.deleteTrainer("R3");
        assertEquals(t, removed);
        assertNull(dao.getTrainer("R3"));
    }

    @Test
    void deleteNonExistingTrainer() {
        Trainer removed = dao.deleteTrainer("NOPE");
        assertNull(removed);
    }

    @Test
    void existsByUsername() {
        Trainer t = new Trainer("X","Y","X.Y","pw",true,
                new TrainingType("HIIT"),"R4");
        dao.createTrainer(t);
        assertTrue(dao.existsByUsername("X.Y"));
        assertFalse(dao.existsByUsername("A.B"));
    }
}