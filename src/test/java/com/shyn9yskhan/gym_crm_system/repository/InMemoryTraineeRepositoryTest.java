package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

class InMemoryTraineeRepositoryTest {

    private InMemoryTraineeRepository repository;
    private Map<String, Trainee> store;

    @BeforeEach
    void setUp() {
        store = spy(new java.util.HashMap<>());
        repository = new InMemoryTraineeRepository(store);
    }

    @Test
    void createAndGetTrainee_andVerifiesPut() {
        Trainee trainee = new Trainee("Alice", "Johnson", "Alice.Johnson", "pw", true,
                LocalDate.of(1990, 5, 20), "123 Main St", "T1");
        repository.createTrainee(trainee);
        verify(store).put("T1", trainee);
        assertEquals(trainee, repository.getTrainee("T1"));
    }

    @Test
    void updateExistingTrainee_andVerifiesPut() {
        Trainee trainee = new Trainee("Bob", "Smith", "Bob.Smith", "pw", true,
                LocalDate.of(1985, 1, 1), "456 Elm", "T2");
        store.put("T2", trainee);
        trainee.setAddress("789 Oak");

        Trainee updated = repository.updateTrainee("T2", trainee);
        assertNotNull(updated);
        assertEquals("789 Oak", updated.getAddress());
        verify(store).put("T2", trainee);
    }

    @Test
    void updateNonExistingTrainee_andVerifiesNoPut() {
        Trainee trainee = repository.updateTrainee("NOPE", null);
        assertNull(trainee);
        verify(store, never()).put(any(), any());
    }

    @Test
    void deleteExistingTrainee_andVerifiesRemove() {
        Trainee trainee = new Trainee("C", "D", "C.D", "pw", true,
                LocalDate.now(), "X", "T3");
        store.put("T3", trainee);

        Trainee removed = repository.deleteTrainee("T3");
        assertEquals(trainee, removed);
        assertNull(repository.getTrainee("T3"));
        verify(store).remove("T3");
    }

    @Test
    void deleteNonExistingTrainee_andVerifiesRemoveCalled() {
        Trainee removed = repository.deleteTrainee("NOPE");
        assertNull(removed);
        verify(store).remove("NOPE");
    }

    @Test
    void existsByUsername_andNoMapModification() {
        Trainee trainee = new Trainee("E", "F", "E.F", "pw", true,
                LocalDate.now(), "Y", "T4");
        store.put("T4", trainee);

        assertTrue(repository.existsByUsername("E.F"));
        assertFalse(repository.existsByUsername("Z.Z"));
        verify(store, never()).put(any(), any());
        verify(store, never()).remove(any());
    }

    @Test
    void findUsernamesByBase_andNoMapModification() {
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "pw", true,
                LocalDate.now(), "A", "T5");
        Trainee trainee1 = new Trainee("John", "Doe", "John.Doe1", "pw", true,
                LocalDate.now(), "B", "T6");
        store.put("T5", trainee);
        store.put("T6", trainee1);

        List<String> list = repository.findUsernamesByBase("John.Doe");
        assertEquals(2, list.size());
        assertTrue(list.contains("John.Doe"));
        assertTrue(list.contains("John.Doe1"));
        verify(store, never()).put(any(), any());
        verify(store, never()).remove(any());
    }
}
