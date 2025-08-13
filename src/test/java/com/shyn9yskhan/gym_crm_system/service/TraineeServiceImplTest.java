package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.domain.Trainee;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.repository.TraineeRepository;
import com.shyn9yskhan.gym_crm_system.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

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
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_successfulCreation_returnsGeneratedIdAndSavesEntity() {
        TraineeDto dto = new TraineeDto();
        dto.setFirstname("John");
        dto.setLastname("Doe");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setAddress("Addr");

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname("John");
        userEntity.setLastname("Doe");
        userEntity.setUsername("John.Doe");
        userEntity.setPassword("pw");
        userEntity.setActive(true);

        UserCreationResult userCreationResult = new UserCreationResult(userEntity);

        when(userService.createUser("John", "Doe")).thenReturn(userCreationResult);

        when(traineeRepository.save(any(TraineeEntity.class))).thenAnswer(invocation -> {
            TraineeEntity e = invocation.getArgument(0);
            e.setId("TRN-1");
            return e;
        });

        String createdId = service.createTrainee(dto);

        assertNotNull(createdId);
        assertEquals("TRN-1", createdId);

        ArgumentCaptor<TraineeEntity> captor = ArgumentCaptor.forClass(TraineeEntity.class);
        verify(traineeRepository).save(captor.capture());
        TraineeEntity saved = captor.getValue();

        assertSame(userEntity, saved.getUser());
        assertEquals(dto.getDateOfBirth(), saved.getDateOfBirth());
        assertEquals(dto.getAddress(), saved.getAddress());
    }

    @Test
    void updateTrainee_notExists_returnsNullAndDoesNotCallUpdate() {
        when(traineeRepository.existsById("no")).thenReturn(false);
        String res = service.updateTrainee("no", LocalDate.now(), "addr");
        assertNull(res);
        verify(traineeRepository, never()).updateTrainee(anyString(), any(), anyString());
    }

    @Test
    void updateTrainee_exists_andUpdateSucceeds_returnsId() {
        String id = "T1";
        when(traineeRepository.existsById(id)).thenReturn(true);
        when(traineeRepository.updateTrainee(eq(id), any(LocalDate.class), eq("newaddr"))).thenReturn(1);
        String res = service.updateTrainee(id, LocalDate.of(1999, 2, 2), "newaddr");

        assertEquals(id, res);
        verify(traineeRepository).updateTrainee(eq(id), any(LocalDate.class), eq("newaddr"));
    }

    @Test
    void updateTrainee_exists_butUpdateFails_returnsNull() {
        String id = "T2";
        when(traineeRepository.existsById(id)).thenReturn(true);
        when(traineeRepository.updateTrainee(eq(id), any(LocalDate.class), eq("newaddr"))).thenReturn(0);
        String res = service.updateTrainee(id, LocalDate.of(1999, 2, 2), "newaddr");

        assertNull(res);
        verify(traineeRepository).updateTrainee(eq(id), any(LocalDate.class), eq("newaddr"));
    }

    @Test
    void deleteTrainee_notExists_returnsNullAndDoesNotDelete() {
        when(traineeRepository.existsById("no")).thenReturn(false);
        String res = service.deleteTrainee("no");
        assertNull(res);
        verify(traineeRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteTrainee_exists_deletesAndReturnsId() {
        String id = "T3";
        when(traineeRepository.existsById(id)).thenReturn(true);
        String res = service.deleteTrainee(id);
        assertEquals(id, res);
        verify(traineeRepository).deleteById(id);
    }

    @Test
    void getTrainee_notFound_returnsNull() {
        when(traineeRepository.findById("no")).thenReturn(Optional.empty());
        assertNull(service.getTrainee("no"));
    }

    @Test
    void getTrainee_found_mapsToDomain() {
        TraineeEntity entity = new TraineeEntity();
        entity.setId("E1");
        entity.setDateOfBirth(LocalDate.of(1995, 5, 5));
        entity.setAddress("Addr");

        UserEntity user = new UserEntity();
        user.setId("U1");
        user.setFirstname("F");
        user.setLastname("L");
        user.setUsername("F.L");
        user.setPassword("pw");
        user.setActive(true);
        entity.setUser(user);

        when(traineeRepository.findById("E1")).thenReturn(Optional.of(entity));

        Trainee domain = service.getTrainee("E1");

        assertNotNull(domain);
        assertEquals("F", domain.getFirstname());
        assertEquals("L", domain.getLastname());
        assertEquals("F.L", domain.getUsername());
        assertEquals("pw", domain.getPassword());
        assertTrue(domain.isActive());
        assertEquals(entity.getDateOfBirth(), domain.getDateOfBirth());
        assertEquals(entity.getAddress(), domain.getAddress());
        assertEquals(user.getId(), domain.getUserId());
    }

    @Test
    void getTraineeEntity_notFound_returnsNull() {
        when(traineeRepository.findById("no")).thenReturn(Optional.empty());
        assertNull(service.getTraineeEntity("no"));
    }

    @Test
    void getTraineeEntity_found_returnsEntity() {
        TraineeEntity entity = new TraineeEntity();
        entity.setId("E2");
        when(traineeRepository.findById("E2")).thenReturn(Optional.of(entity));
        TraineeEntity res = service.getTraineeEntity("E2");
        assertNotNull(res);
        assertEquals("E2", res.getId());
    }

    @Test
    void saveTrainee_delegatesToRepository_andReturnsSaved() {
        TraineeEntity entity = new TraineeEntity();
        entity.setId("S1");
        when(traineeRepository.save(entity)).thenReturn(entity);
        TraineeEntity res = service.saveTrainee(entity);
        assertNotNull(res);
        assertEquals("S1", res.getId());
        verify(traineeRepository).save(entity);
    }
}
