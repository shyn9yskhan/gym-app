package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
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

class InMemoryTrainerRepositoryTest {

    private InMemoryTrainerRepository repository;
    private Map<String, Trainer> store;

    @BeforeEach
    void setUp() {
        store = spy(new HashMap<>());
        repository = new InMemoryTrainerRepository(store);
    }

    @Test
    void createAndGetTrainer_andVerifiesPut() {
        Trainer trainer = new Trainer("Janex","Does","Janex.Does","pw",true,
                new TrainingType("CARDIO"),"R145");
        repository.createTrainer(trainer);
        verify(store).put("R145", trainer);
        assertEquals(trainer, repository.getTrainer("R145"));
    }

    @Test
    void updateExistingTrainer_andVerifiesPut() {
        Trainer trainer = new Trainer("Sam","Lee","Sam.Lee","pw",true,
                new TrainingType("STRENGTH"),"R2");
        store.put("R2", trainer);
        trainer.setLastname("Li");

        Trainer updated = repository.updateTrainer("R2", trainer);
        assertNotNull(updated);
        assertEquals("Li", updated.getLastname());
        verify(store).put("R2", trainer);
    }

    @Test
    void updateNonExistingTrainer_andVerifiesNoPut() {
        Trainer trainer = repository.updateTrainer("NOPE", null);
        assertNull(trainer);
        verify(store, never()).put(any(), any());
    }

    @Test
    void deleteExistingTrainer_andVerifiesRemove() {
        Trainer trainer = new Trainer("A","B","A.B","pw",true,
                new TrainingType("YOGA"),"R3");
        store.put("R3", trainer);

        Trainer removed = repository.deleteTrainer("R3");
        assertEquals(trainer, removed);
        assertNull(repository.getTrainer("R3"));
        verify(store).remove("R3");
    }

    @Test
    void deleteNonExistingTrainer_andVerifiesRemoveCalled() {
        Trainer removed = repository.deleteTrainer("NOPE");
        assertNull(removed);
        verify(store).remove("NOPE");
    }

    @Test
    void existsByUsername_andNoMapModification() {
        Trainer trainer = new Trainer("X","Y","X.Y","pw",true,
                new TrainingType("HIIT"),"R4");
        store.put("R4", trainer);

        assertTrue(repository.existsByUsername("X.Y"));
        assertFalse(repository.existsByUsername("A.B"));
        verify(store, never()).put(any(), any());
        verify(store, never()).remove(any());
    }

    @Test
    void findUsernamesByBase_andNoMapModification() {
        Trainer trainer = new Trainer("John","Doe","John.Doe","pw",true,
                new TrainingType("CARDIO"),"R5");
        Trainer trainer1 = new Trainer("John","Doe","John.Doe1","pw",true,
                new TrainingType("CARDIO"),"R6");
        store.put("R5", trainer);
        store.put("R6", trainer1);

        var list = repository.findUsernamesByBase("John.Doe");
        assertEquals(2, list.size());
        assertTrue(list.contains("John.Doe"));
        assertTrue(list.contains("John.Doe1"));
        verify(store, never()).put(any(), any());
        verify(store, never()).remove(any());
    }
}
