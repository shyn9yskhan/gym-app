package com.shyn9yskhan.gym_crm_system.repository;

import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

class InMemoryTrainingRepositoryTest {

    private InMemoryTrainingRepository repository;
    private Map<String, Training> store;

    @BeforeEach
    void setUp() {
        store = spy(new java.util.HashMap<>());
        repository = new InMemoryTrainingRepository(store);
    }

    @Test
    void createAndGetTraining_andVerifiesPut() {
        Training training = new Training("TR1","R1","Workout",
                new TrainingType("CARDIO"),
                LocalDateTime.now(),Duration.ofHours(1));
        repository.createTraining(training);
        verify(store).put("TR1", training);
        assertEquals(training, repository.getTraining("TR1"));
    }

    @Test
    void updateExistingTraining_andVerifiesPut() {
        Training training = new Training("TR2","R2","Yoga",
                new TrainingType("YOGA"),
                LocalDateTime.now(),Duration.ofMinutes(45));
        store.put("TR2", training);
        training.setName("Power Yoga");

        Training updated = repository.updateTraining("TR2", training);
        assertNotNull(updated);
        assertEquals("Power Yoga", updated.getName());
        verify(store).put("TR2", training);
    }

    @Test
    void updateNonExistingTraining_andVerifiesNoPut() {
        Training training = repository.updateTraining("NOPE", null);
        assertNull(training);
        verify(store, never()).put(any(), any());
    }

    @Test
    void deleteExistingTraining_andVerifiesRemove() {
        Training training = new Training("TR3","R3","Pilates",
                new TrainingType("PILATES"),
                LocalDateTime.now(),Duration.ofHours(1));
        store.put("TR3", training);

        Training removed = repository.deleteTraining("TR3");
        assertEquals(training, removed);
        assertNull(repository.getTraining("TR3"));
        verify(store).remove("TR3");
    }

    @Test
    void deleteNonExistingTraining_andVerifiesRemoveCalled() {
        Training removed = repository.deleteTraining("NOPE");
        assertNull(removed);
        verify(store).remove("NOPE");
    }
}
