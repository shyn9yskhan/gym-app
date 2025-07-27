package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDAOTest {

    private TrainerDAO dao;

    @BeforeEach
    void setUp() {
        Map<String, Trainer> store = new HashMap<>();
        dao = new TrainerDAO(store);
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
        assertTrue(dao.updateTrainer("R2", t));
        assertEquals("Li", dao.getTrainer("R2").getLastname());
    }

    @Test
    void updateNonExistingTrainer() {
        assertFalse(dao.updateTrainer("NOPE", null));
    }

    @Test
    void deleteTrainer() {
        Trainer t = new Trainer("A","B","A.B","pw",true,
                new TrainingType("YOGA"),"R3");
        dao.createTrainer(t);
        assertTrue(dao.deleteTrainer("R3"));
        assertNull(dao.getTrainer("R3"));
    }

    @Test
    void deleteNonExistingTrainer() {
        assertFalse(dao.deleteTrainer("NOPE"));
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