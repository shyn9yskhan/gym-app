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
import static org.mockito.Mockito.times;
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
        AddTrainingRequest req = new AddTrainingRequest();
        req.setTraineeUsername("traineeUser");
        req.setTrainerUsername("trainerUser");
        req.setTrainingName("Morning Cardio");
        req.setTrainingDate(LocalDate.of(2025, 1, 1));
        req.setTrainingDuration(60);

        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setId("trainee-1");
        traineeEntity.setTrainers(new HashSet<>());

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setId("trainer-1");
        trainerEntity.setTrainees(new HashSet<>());

        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId("tt-1");
        trainingTypeEntity.setTrainingTypeName("CARDIO");
        trainerEntity.setSpecialization(trainingTypeEntity);

        when(traineeService.getTraineeEntityByUsername("traineeUser")).thenReturn(traineeEntity);
        when(trainerService.getTrainerEntityByUsername("trainerUser")).thenReturn(trainerEntity);

        when(trainingRepository.save(any(TrainingEntity.class))).thenAnswer(invocation -> {
            TrainingEntity t = invocation.getArgument(0);
            t.setId("training-uuid-1");
            return t;
        });

        String createdId = service.addTraining(req);

        assertNotNull(createdId);
        assertEquals("training-uuid-1", createdId);

        ArgumentCaptor<TrainingEntity> captor = ArgumentCaptor.forClass(TrainingEntity.class);
        verify(trainingRepository, times(1)).save(captor.capture());
        TrainingEntity saved = captor.getValue();

        assertEquals("trainee-1", saved.getTrainee().getId());
        assertEquals("trainer-1", saved.getTrainer().getId());
        assertEquals("Morning Cardio", saved.getTrainingName());
        assertEquals(trainingTypeEntity, saved.getTrainingType());
        assertEquals(LocalDate.of(2025, 1, 1), saved.getTrainingDate());
        assertEquals(60, saved.getTrainingDuration());

        verify(traineeService, times(1)).saveTrainee(traineeEntity);
        verify(trainerService, times(1)).saveTrainer(trainerEntity);

        assertTrue(traineeEntity.getTrainers().contains(trainerEntity));
        assertTrue(trainerEntity.getTrainees().contains(traineeEntity));
    }

    @Test
    void addTraining_missingEntities_returnsNull_andDoesNotSave() {
        AddTrainingRequest req = new AddTrainingRequest();
        req.setTraineeUsername("missing");
        req.setTrainerUsername("trainerUser");
        req.setTrainingName("X");
        req.setTrainingDate(LocalDate.now());
        req.setTrainingDuration(30);

        when(traineeService.getTraineeEntityByUsername("missing")).thenReturn(null);

        String result = service.addTraining(req);

        assertNull(result);
        verify(trainingRepository, never()).save(any());
        verify(trainerService, never()).saveTrainer(any());
        verify(traineeService, never()).saveTrainee(any());
    }

    @Test
    void getTraining_existing_returnsMappedDomainAndVerifiesRepositoryCall() {
        TrainingEntity te = new TrainingEntity();
        te.setId("tr-1");

        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setId("trainee-1");
        te.setTrainee(traineeEntity);

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setId("trainer-1");
        te.setTrainer(trainerEntity);

        te.setTrainingName("Evening Yoga");

        TrainingTypeEntity tte = new TrainingTypeEntity();
        tte.setTrainingTypeName("YOGA");
        te.setTrainingType(tte);

        te.setTrainingDate(LocalDate.of(2025, 5, 5));
        te.setTrainingDuration(45);

        when(trainingRepository.findById("tr-1")).thenReturn(Optional.of(te));

        Training result = service.getTraining("tr-1");

        assertNotNull(result);
        assertEquals("trainee-1", result.getTraineeId());
        assertEquals("trainer-1", result.getTrainerId());
        assertEquals("Evening Yoga", result.getName());
        assertEquals("YOGA", result.getType().getTrainingTypeName());
        assertEquals(LocalDate.of(2025, 5, 5), result.getDate());
        assertEquals(45, result.getDuration());

        verify(trainingRepository, times(1)).findById("tr-1");
    }

    @Test
    void getTraining_notFound_returnsNull_andVerifiesRepositoryCall() {
        when(trainingRepository.findById("nope")).thenReturn(Optional.empty());
        Training result = service.getTraining("nope");
        assertNull(result);
        verify(trainingRepository, times(1)).findById("nope");
    }
}
