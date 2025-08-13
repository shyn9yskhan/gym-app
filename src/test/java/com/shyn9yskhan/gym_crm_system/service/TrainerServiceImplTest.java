package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.domain.Trainer;
import com.shyn9yskhan.gym_crm_system.domain.TrainingType;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainerRepository;
import com.shyn9yskhan.gym_crm_system.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainerServiceImpl service;

    @Test
    void createTrainer_successfulCreation_returnsGeneratedIdAndSavesEntity() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstname("Zack");
        trainerDto.setLastname("Lion");
        trainerDto.setTrainingTypeName("BOXING");

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname("Zack");
        userEntity.setLastname("Lion");
        userEntity.setUsername("Zack.Lion");
        userEntity.setPassword("pass123");
        userEntity.setActive(true);

        UserCreationResult userCreationResult = new UserCreationResult(userEntity);

        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity();
        trainingTypeEntity.setId("TT1");
        trainingTypeEntity.setTrainingTypeName("BOXING");

        when(userService.createUser(trainerDto.getFirstname(), trainerDto.getLastname()))
                .thenReturn(userCreationResult);

        when(trainingTypeService.getTrainingTypeByName("BOXING"))
                .thenReturn(trainingTypeEntity);

        when(repository.save(any(TrainerEntity.class))).thenAnswer(invocation -> {
            TrainerEntity arg = invocation.getArgument(0);
            arg.setId("TR-123");
            return arg;
        });

        String createdId = service.createTrainer(trainerDto);

        assertNotNull(createdId);
        assertEquals("TR-123", createdId);

        ArgumentCaptor<TrainerEntity> captor = ArgumentCaptor.forClass(TrainerEntity.class);
        verify(repository).save(captor.capture());
        TrainerEntity saved = captor.getValue();

        assertSame(userEntity, saved.getUser(), "UserEntity should be set on saved TrainerEntity");
        assertSame(trainingTypeEntity, saved.getSpecialization(), "TrainingTypeEntity should be set");
    }

    @Test
    void createTrainer_whenTrainingTypeNotFound_returnsNullAndDoesNotSave() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstname("Anna");
        trainerDto.setLastname("Bell");
        trainerDto.setTrainingTypeName("UNKNOWN");

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname("Anna");
        userEntity.setLastname("Bell");

        when(userService.createUser(trainerDto.getFirstname(), trainerDto.getLastname()))
                .thenReturn(new UserCreationResult(userEntity));

        when(trainingTypeService.getTrainingTypeByName("UNKNOWN"))
                .thenReturn(null);

        String result = service.createTrainer(trainerDto);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void updateTrainer_notExists_returnsNull() {
        String id = "nonexistent";
        when(repository.existsById(id)).thenReturn(false);

        String res = service.updateTrainer(id, "CARDIO");

        assertNull(res);
        verify(repository, never()).updateTrainerSpecialization(anyString(), any());
    }

    @Test
    void updateTrainer_trainingTypeNotFound_returnsNull() {
        String id = "T1";
        when(repository.existsById(id)).thenReturn(true);
        when(trainingTypeService.getTrainingTypeByName("UNKNOWN")).thenReturn(null);

        String res = service.updateTrainer(id, "UNKNOWN");

        assertNull(res);
        verify(repository, never()).updateTrainerSpecialization(anyString(), any());
    }

    @Test
    void updateTrainer_success_returnsId() {
        String id = "T2";
        when(repository.existsById(id)).thenReturn(true);

        TrainingTypeEntity tte = new TrainingTypeEntity();
        tte.setId("TT2");
        tte.setTrainingTypeName("STRENGTH");
        when(trainingTypeService.getTrainingTypeByName("STRENGTH")).thenReturn(tte);

        when(repository.updateTrainerSpecialization(eq(id), eq(tte))).thenReturn(1);

        String res = service.updateTrainer(id, "STRENGTH");

        assertEquals(id, res);
        verify(repository).updateTrainerSpecialization(eq(id), eq(tte));
    }

    @Test
    void getTrainer_whenNotFound_returnsNull() {
        when(repository.findById("no")).thenReturn(Optional.empty());
        Trainer res = service.getTrainer("no");
        assertNull(res);
    }

    @Test
    void getTrainer_whenFound_mapsEntityToDomain() {
        TrainerEntity entity = new TrainerEntity();
        entity.setId("E1");

        UserEntity u = new UserEntity();
        u.setFirstname("First");
        u.setLastname("Last");
        u.setUsername("First.Last");
        u.setPassword("pwd");
        u.setActive(true);
        entity.setUser(u);

        TrainingTypeEntity tte = new TrainingTypeEntity();
        tte.setId("TT");
        tte.setTrainingTypeName("YOGA");
        entity.setSpecialization(tte);

        when(repository.findById("E1")).thenReturn(Optional.of(entity));

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
    void getTrainerEntity_whenNotFound_returnsNull() {
        when(repository.findById("no")).thenReturn(Optional.empty());
        assertNull(service.getTrainerEntity("no"));
    }

    @Test
    void getTrainerEntity_whenFound_returnsEntity() {
        TrainerEntity entity = new TrainerEntity();
        entity.setId("E2");
        when(repository.findById("E2")).thenReturn(Optional.of(entity));

        TrainerEntity res = service.getTrainerEntity("E2");
        assertNotNull(res);
        assertEquals("E2", res.getId());
    }

    @Test
    void saveTrainer_delegatesToRepository_andReturnsSaved() {
        TrainerEntity entity = new TrainerEntity();
        entity.setId("S1");

        when(repository.save(entity)).thenReturn(entity);

        TrainerEntity res = service.saveTrainer(entity);
        assertNotNull(res);
        assertEquals("S1", res.getId());
        verify(repository).save(entity);
    }
}
