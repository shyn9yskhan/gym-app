package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.client.TraineeClient;
import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.dto.CreateTraineeResponse;
import com.shyn9yskhan.gym_crm_system.dto.UpdateTraineeRequest;
import com.shyn9yskhan.gym_crm_system.dto.UpdateTraineesTrainerListRequest;
import com.shyn9yskhan.gym_crm_system.dto.TrainerProfileDto;
import com.shyn9yskhan.gym_crm_system.entity.TraineeEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.repository.TraineeRepository;
import com.shyn9yskhan.gym_crm_system.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TraineeClient traineeClient;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_success_returnsUsernameAndPassword() {
        TraineeDto dto = new TraineeDto();
        dto.setFirstname("John");
        dto.setLastname("Doe");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setAddress("Addr");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("John.Doe");
        userEntity.setPassword("pw");

        when(traineeClient.userService_createUser("John", "Doe"))
                .thenReturn(new UserCreationResult(userEntity));

        when(traineeRepository.save(any(TraineeEntity.class))).thenAnswer(invocation -> {
            TraineeEntity e = invocation.getArgument(0);
            e.setId("T1");
            return e;
        });

        CreateTraineeResponse response = service.createTrainee(dto);

        assertNotNull(response);
        assertEquals("John.Doe", response.getUsername());
        assertEquals("pw", response.getPassword());
    }

    @Test
    void updateTrainee_success_returnsProfile() {
        UpdateTraineeRequest request = new UpdateTraineeRequest();
        request.setUsername("john.doe");
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setAddress("New Addr");
        request.setActive(true);

        TraineeEntity found = new TraineeEntity();
        found.setId("T-1");
        UserEntity ue = new UserEntity();
        ue.setId("U-1");
        ue.setUsername("john.doe");
        ue.setFirstname("Old");
        ue.setLastname("Name");
        ue.setActive(true);
        found.setUser(ue);
        found.setDateOfBirth(LocalDate.of(1980,1,1));
        found.setAddress("Old Addr");

        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(found));
        when(traineeRepository.updateTrainee(eq("T-1"), any(LocalDate.class), eq("New Addr"))).thenReturn(1);

        com.shyn9yskhan.gym_crm_system.dto.UserDto returnedUserDto = new com.shyn9yskhan.gym_crm_system.dto.UserDto();
        returnedUserDto.setUsername("john.doe");
        returnedUserDto.setFirstname("John");
        returnedUserDto.setLastname("Doe");
        returnedUserDto.setActive(true);

        when(traineeClient.userService_updateUser(any(com.shyn9yskhan.gym_crm_system.dto.UserDto.class)))
                .thenReturn(returnedUserDto);

        TraineeEntity after = new TraineeEntity();
        after.setId("T-1");
        UserEntity afterUser = new UserEntity();
        afterUser.setUsername("john.doe");
        afterUser.setFirstname("John");
        afterUser.setLastname("Doe");
        afterUser.setActive(true);
        after.setUser(afterUser);
        after.setDateOfBirth(request.getDateOfBirth());
        after.setAddress(request.getAddress());
        when(traineeRepository.findById("T-1")).thenReturn(Optional.of(after));

        com.shyn9yskhan.gym_crm_system.dto.TraineeProfile profile = service.updateTrainee(request);

        assertNotNull(profile);
        assertEquals("john.doe", profile.getUsername());
        assertEquals("John", profile.getFirstname());
        assertEquals("Doe", profile.getLastname());
        assertEquals(request.getDateOfBirth(), profile.getDateOfBirth());
        assertEquals("New Addr", profile.getAddress());

        verify(traineeRepository).findByUserUsername("john.doe");
        verify(traineeRepository).updateTrainee(eq("T-1"), any(LocalDate.class), eq("New Addr"));
        verify(traineeClient).userService_updateUser(any(com.shyn9yskhan.gym_crm_system.dto.UserDto.class));
        verify(traineeRepository).findById("T-1");
    }

    @Test
    void deleteTrainee_success_returnsUsername() {
        String username = "alice";
        when(traineeRepository.existsByUserUsername(username)).thenReturn(true);
        when(traineeRepository.deleteByUser_Username(username)).thenReturn(1L);
        when(traineeClient.userService_deleteUser(username)).thenReturn(username);
        String result = service.deleteTrainee(username);
        assertEquals(username, result);
        verify(traineeRepository).existsByUserUsername(username);
        verify(traineeRepository).deleteByUser_Username(username);
        verify(traineeClient).userService_deleteUser(username);
    }

    @Test
    void getTrainee_success_returnsEntity() {
        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setId("T1");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("John.Doe");
        traineeEntity.setUser(userEntity);

        when(traineeRepository.findById("T1"))
                .thenReturn(Optional.of(traineeEntity));

        TraineeEntity result = service.getTraineeEntity("T1");

        assertNotNull(result);
        assertEquals("John.Doe", result.getUser().getUsername());
    }

    @Test
    void updateTraineesTrainerList_success_returnsTrainers() {
        UpdateTraineesTrainerListRequest request = new UpdateTraineesTrainerListRequest();
        request.setTraineeUsername("tom");
        request.setTrainers(Arrays.asList("tr1", "tr2"));

        TraineeEntity trainee = new TraineeEntity();
        trainee.setId("T100");
        UserEntity traineeUser = new UserEntity();
        traineeUser.setUsername("tom");
        trainee.setUser(traineeUser);

        when(traineeRepository.findByUserUsername("tom")).thenReturn(Optional.of(trainee));

        TrainerEntity trainerA = new TrainerEntity();
        trainerA.setId("TR-A");
        TrainingTypeEntity trainingType0 = new TrainingTypeEntity();
        trainingType0.setId("TTP0");
        trainingType0.setTrainingTypeName("boxing");
        trainerA.setSpecialization(trainingType0);
        UserEntity trainerAUser = new UserEntity();
        trainerAUser.setUsername("tr1");
        trainerAUser.setFirstname("T1");
        trainerAUser.setLastname("One");
        trainerA.setUser(trainerAUser);

        TrainerEntity trainerB = new TrainerEntity();
        trainerB.setId("TR-B");
        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setId("TTP1");
        trainingType.setTrainingTypeName("football");
        trainerB.setSpecialization(trainingType);
        UserEntity trainerBUser = new UserEntity();
        trainerBUser.setUsername("tr2");
        trainerBUser.setFirstname("T2");
        trainerBUser.setLastname("Two");
        trainerB.setUser(trainerBUser);

        when(traineeClient.trainerService_findAllByUserUsernameIn(request.getTrainers()))
                .thenReturn(Arrays.asList(trainerA, trainerB));

        when(traineeRepository.save(any(TraineeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<TrainerProfileDto> result = service.updateTraineesTrainerList(request);

        assertNotNull(result);
        assertEquals(2, result.size());
        Set<String> usernames = new HashSet<>();
        for (TrainerProfileDto dto : result) usernames.add(dto.getUsername());
        assertTrue(usernames.contains("tr1"));
        assertTrue(usernames.contains("tr2"));

        verify(traineeRepository).findByUserUsername("tom");
        verify(traineeClient).trainerService_findAllByUserUsernameIn(request.getTrainers());
        verify(traineeRepository).save(any(TraineeEntity.class));
    }
}
