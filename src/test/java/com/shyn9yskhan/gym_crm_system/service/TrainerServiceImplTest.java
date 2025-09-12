package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.domain.Trainer;
import com.shyn9yskhan.gym_crm_system.domain.TrainingType;
import com.shyn9yskhan.gym_crm_system.dto.CreateTrainerResponse;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.dto.TrainingTypeDto;
import com.shyn9yskhan.gym_crm_system.dto.TrainerProfile;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.service.impl.TrainerServiceImpl;
import com.shyn9yskhan.gym_crm_system.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainerServiceImpl service;

    @Test
    void createTrainer_successfulCreation_returnsCreateTrainerResponse_andPersists() {
        TrainerDto request = new TrainerDto();
        request.setFirstname("Zack");
        request.setLastname("Lion");
        request.setTrainingTypeName("BOXING");
        UserEntity createdUser = new UserEntity("U1", "Zack", "Lion", "Zack.Lion", "pass123", true);
        when(userService.createUser(request.getFirstname(), request.getLastname())).thenReturn(new UserCreationResult(createdUser));
        TrainingTypeEntity tte = new TrainingTypeEntity("TT1", "BOXING");
        when(trainingTypeService.getTrainingTypeByName("BOXING")).thenReturn(tte);
        when(trainerRepository.save(any(TrainerEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CreateTrainerResponse response = service.createTrainer(request);

        assertNotNull(response);
        assertEquals("Zack.Lion", response.getUsername());
        assertEquals("pass123", response.getPassword());

        ArgumentCaptor<TrainerEntity> captor = ArgumentCaptor.forClass(TrainerEntity.class);
        verify(trainerRepository).save(captor.capture());
        TrainerEntity saved = captor.getValue();
        assertSame(createdUser, saved.getUser());
        assertSame(tte, saved.getSpecialization());
    }

    @Test
    void createTrainer_whenTrainingTypeMissing_returnsNull_butUserCreatedIsCalled() {
        TrainerDto request = new TrainerDto();
        request.setFirstname("Anna");
        request.setLastname("Bell");
        request.setTrainingTypeName("UNKNOWN");
        UserEntity createdUser = new UserEntity(null, "Anna", "Bell", null, null, false);
        when(userService.createUser(request.getFirstname(), request.getLastname())).thenReturn(new UserCreationResult(createdUser));
        when(trainingTypeService.getTrainingTypeByName("UNKNOWN")).thenReturn(null);
        CreateTrainerResponse response = service.createTrainer(request);
        assertNull(response);
        verify(userService).createUser("Anna", "Bell");
        verify(trainerRepository, never()).save(any());
    }

    @Test
    void updateTrainer_notFoundByUsername_returnsNull_andDoesNotUpdate() {
        TrainingTypeDto specialization = new TrainingTypeDto();
        specialization.setId("TT");
        specialization.setTrainingTypeName("YOGA");

        var req = new com.shyn9yskhan.gym_crm_system.dto.UpdateTrainerRequest();
        req.setUsername("noone");
        req.setFirstname("X");
        req.setLastname("Y");
        req.setActive(true);
        req.setSpecialization(specialization);

        when(trainerRepository.findByUserUsername("noone")).thenReturn(Optional.empty());

        TrainerProfile res = service.updateTrainer(req);
        assertNull(res);
        verify(trainerRepository, never()).updateTrainer(anyString(), any());
        verify(userService, never()).updateUser(any());
    }

    @Test
    void getTrainer_notFound_returnsNull() {
        when(trainerRepository.findById("no")).thenReturn(Optional.empty());
        assertNull(service.getTrainer("no"));
    }

    @Test
    void getTrainer_found_mapsToDomain() {
        UserEntity user = new UserEntity("U-1", "First", "Last", "First.Last", "pwd", true);
        TrainingTypeEntity tte = new TrainingTypeEntity("TT-YOGA", "YOGA");
        TrainerEntity entity = new TrainerEntity("E1", tte, user, new HashSet<>());
        when(trainerRepository.findById("E1")).thenReturn(Optional.of(entity));
        Trainer domain = service.getTrainer("E1");

        assertNotNull(domain);
        assertEquals("First", domain.getFirstname());
        assertEquals("Last", domain.getLastname());
        assertEquals("First.Last", domain.getUsername());
        assertEquals("pwd", domain.getPassword());
        assertTrue(domain.isActive());
        assertEquals(new TrainingType("YOGA"), domain.getSpecialization());
    }

    @Test
    void getTrainerEntity_whenFound_returnsEntity() {
        TrainerEntity entity = new TrainerEntity("E2", null, null, new HashSet<>());
        when(trainerRepository.findById("E2")).thenReturn(Optional.of(entity));
        TrainerEntity res = service.getTrainerEntity("E2");
        assertNotNull(res);
        assertEquals("E2", res.getId());
    }

    @Test
    void saveTrainer_delegatesToRepository_andReturnsSaved() {
        TrainerEntity entity = new TrainerEntity("S1", null, null, new HashSet<>());
        when(trainerRepository.save(entity)).thenReturn(entity);
        TrainerEntity res = service.saveTrainer(entity);
        assertNotNull(res);
        assertEquals("S1", res.getId());
        verify(trainerRepository).save(entity);
    }
}
