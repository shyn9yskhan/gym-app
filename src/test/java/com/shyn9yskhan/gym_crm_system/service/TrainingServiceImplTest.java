package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTrainingRepository;
import com.shyn9yskhan.gym_crm_system.dto.TrainingDto;
import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceImplTest {

    @Mock
    private InMemoryTrainingRepository inMemoryTrainingRepository;

    @InjectMocks
    private TrainingServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraining_persistsWithGeneratedId() {
        TrainingDto dto = new TrainingDto();
        dto.setTrainerId("R1");
        dto.setTrainingName("Workout");
        dto.setTrainingTypeName("CARDIO");
        dto.setTrainingDate(LocalDateTime.of(2025,1,1,10,0));
        dto.setTrainingDuration(Duration.ofHours(1));

        when(inMemoryTrainingRepository.createTraining(any())).thenAnswer(i -> i.getArgument(0));

        Training created = service.createTraining(dto);
        assertNotNull(created.getId());
        assertEquals(dto.getTrainerId(), created.getTrainerId());
        assertEquals(dto.getTrainingName(), created.getName());
    }

    @Test
    void getTraining_delegatesToDAO() {
        Training t = new Training("T1","R1","Name",
                new TrainingType("YOGA"),
                LocalDateTime.now(), Duration.ofMinutes(30));
        when(inMemoryTrainingRepository.getTraining("T1")).thenReturn(t);
        assertSame(t, service.getTraining("T1"));
    }
}