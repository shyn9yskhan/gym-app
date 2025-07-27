package com.shyn9yskhan.gym_crm_system.dao;

import com.shyn9yskhan.gym_crm_system.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDAOTest {

    private TraineeDAO dao;

    @BeforeEach
    void setUp() {
        Map<String, Trainee> store = new HashMap<>();
        dao = new TraineeDAO(store);
    }

    @Test
    void createAndGetTrainee() {
        Trainee t = new Trainee("Alice", "Johnson", "Alice.Johnson", "pw", true,
                LocalDate.of(1990,5,20), "123 Main St", "T1");
        dao.createTrainee(t);
        assertEquals(t, dao.getTrainee("T1"));
    }

    @Test
    void updateExistingTrainee() {
        Trainee t = new Trainee("Bob", "Smith", "Bob.Smith", "pw", true,
                LocalDate.of(1985,1,1), "456 Elm", "T2");
        dao.createTrainee(t);
        t.setAddress("789 Oak");
        assertTrue(dao.updateTrainee("T2", t));
        assertEquals("789 Oak", dao.getTrainee("T2").getAddress());
    }

    @Test
    void updateNonExistingTrainee() {
        assertFalse(dao.updateTrainee("NOPE", null));
    }

    @Test
    void deleteTrainee() {
        Trainee t = new Trainee("C", "D", "C.D", "pw", true,
                LocalDate.now(), "X", "T3");
        dao.createTrainee(t);
        assertTrue(dao.deleteTrainee("T3"));
        assertNull(dao.getTrainee("T3"));
    }

    @Test
    void deleteNonExistingTrainee() {
        assertFalse(dao.deleteTrainee("NOPE"));
    }

    @Test
    void existsByUsername() {
        Trainee t = new Trainee("E", "F", "E.F", "pw", true,
                LocalDate.now(), "Y", "T4");
        dao.createTrainee(t);
        assertTrue(dao.existsByUsername("E.F"));
        assertFalse(dao.existsByUsername("Z.Z"));
    }

    @Test
    void findUsernamesByBase() {
        Trainee t1 = new Trainee("John", "Doe", "John.Doe", "pw", true,
                LocalDate.now(), "A", "T5");
        Trainee t2 = new Trainee("John", "Doe", "John.Doe1", "pw", true,
                LocalDate.now(), "B", "T6");
        dao.createTrainee(t1);
        dao.createTrainee(t2);
        List<String> list = dao.findUsernamesByBase("John.Doe");
        assertEquals(2, list.size());
        assertTrue(list.contains("John.Doe"));
        assertTrue(list.contains("John.Doe1"));
    }
}