package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.AddTrainingRequest;
import com.shyn9yskhan.gym_crm_system.domain.Training;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainingRepository;
import com.shyn9yskhan.gym_crm_system.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingServiceImpl service;

    @Test
    void addTraining_success_savesTrainingAndAssociations_andVerifiesMocks() {
        AddTrainingRequest request = new AddTrainingRequest("traineeUser", "trainerUser", "Morning Cardio", LocalDate.of(2025, 1, 1), 60);
        TraineeEntity traineeEntity = new TraineeEntity("trainee-1", null, null, null, new HashSet<>());
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity("tt-1", "CARDIO");
        TrainerEntity trainerEntity = new TrainerEntity("trainer-1", trainingTypeEntity, null, new HashSet<>());

        when(traineeService.getTraineeEntityByUsername("traineeUser")).thenReturn(traineeEntity);
        when(trainerService.getTrainerEntityByUsername("trainerUser")).thenReturn(trainerEntity);
        when(trainingRepository.save(any(TrainingEntity.class))).thenAnswer(invocation -> {
            TrainingEntity trainingEntity = invocation.getArgument(0);
            trainingEntity.setId("training-uuid-1");
            return trainingEntity;
        });

        String createdId = service.addTraining(request);
        assertNotNull(createdId);
        assertEquals("training-uuid-1", createdId);
        ArgumentCaptor<TrainingEntity> captor = ArgumentCaptor.forClass(TrainingEntity.class);
        verify(trainingRepository).save(captor.capture());
        TrainingEntity saved = captor.getValue();

        assertEquals("trainee-1", saved.getTrainee().getId());
        assertEquals("trainer-1", saved.getTrainer().getId());
        assertEquals("Morning Cardio", saved.getTrainingName());
        assertEquals(trainingTypeEntity, saved.getTrainingType());
        assertEquals(LocalDate.of(2025, 1, 1), saved.getTrainingDate());
        assertEquals(60, saved.getTrainingDuration());

        verify(traineeService).getTraineeEntityByUsername("traineeUser");
        verify(trainerService).getTrainerEntityByUsername("trainerUser");
        verify(traineeService).saveTrainee(traineeEntity);
        verify(trainerService).saveTrainer(trainerEntity);
        assertTrue(traineeEntity.getTrainers().contains(trainerEntity));
        assertTrue(trainerEntity.getTrainees().contains(traineeEntity));
    }

    @Test
    void addTraining_missingEntities_returnsNull_andDoesNotSave() {
        AddTrainingRequest request = new AddTrainingRequest("missing", "trainerUser", "X", LocalDate.now(), 30);
        when(traineeService.getTraineeEntityByUsername("missing")).thenReturn(null);
        String result = service.addTraining(request);
        assertNull(result);
        verify(traineeService).getTraineeEntityByUsername("missing");
        verify(trainingRepository, never()).save(any());
        verify(trainerService, never()).saveTrainer(any());
        verify(traineeService, never()).saveTrainee(any());
    }

    @Test
    void getTraining_existing_returnsMappedDomainAndVerifiesRepositoryCall() {
        TraineeEntity traineeEntity = new TraineeEntity("trainee-1", null, null, null, new HashSet<>());
        TrainerEntity trainerEntity = new TrainerEntity("trainer-1", null, null, new HashSet<>());
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity("trainingTypeEntity-1", "YOGA");
        TrainingEntity trainingEntity = new TrainingEntity("tr-1", traineeEntity, trainerEntity, "Evening Yoga", trainingTypeEntity, LocalDate.of(2025, 5, 5), 45);

        when(trainingRepository.findById("tr-1")).thenReturn(Optional.of(trainingEntity));
        Training result = service.getTraining("tr-1");
        assertNotNull(result);
        assertEquals("trainee-1", result.getTraineeId());
        assertEquals("trainer-1", result.getTrainerId());
        assertEquals("Evening Yoga", result.getName());
        assertEquals("YOGA", result.getType().getTrainingTypeName());
        assertEquals(LocalDate.of(2025, 5, 5), result.getDate());
        assertEquals(45, result.getDuration());
        verify(trainingRepository).findById("tr-1");
    }

    @Test
    void getTraining_notFound_returnsNull_andVerifiesRepositoryCall() {
        when(trainingRepository.findById("nope")).thenReturn(Optional.empty());
        Training result = service.getTraining("nope");
        assertNull(result);
        verify(trainingRepository).findById("nope");
    }
}
