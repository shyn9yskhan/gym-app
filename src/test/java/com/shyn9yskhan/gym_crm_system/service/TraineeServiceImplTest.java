package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTraineeRepository;
import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    @Mock
    private InMemoryTraineeRepository traineeDAO;

    @InjectMocks
    private TraineeServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_generatesUniqueUsernameAndPersists() {
        TraineeDto dto = new TraineeDto("John","Doe",LocalDate.of(1990,1,1),"Addr");

        when(traineeDAO.existsByUsername("John.Doe")).thenReturn(true);
        when(traineeDAO.findUsernamesByBase("John.Doe"))
                .thenReturn(Arrays.asList("John.Doe","John.Doe1","John.Doe2"));

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        when(traineeDAO.createTrainee(any())).thenAnswer(i -> i.getArgument(0));

        Trainee created = service.createTrainee(dto);

        verify(traineeDAO).createTrainee(captor.capture());
        Trainee persisted = captor.getValue();
        assertEquals("John.Doe3", persisted.getUsername());
        assertNotNull(persisted.getUserId());
        assertEquals(dto.getDateOfBirth(), persisted.getDateOfBirth());
        assertEquals(dto.getAddress(), persisted.getAddress());
        assertSame(created, persisted);
    }

    @Test
    void updateTrainee_nonExisting_returnsNull() {
        when(traineeDAO.getTrainee("X")).thenReturn(null);

        Trainee result = service.updateTrainee("X", new TraineeDto());
        assertNull(result);
        verify(traineeDAO).getTrainee("X");
    }

    @Test
    void updateTrainee_existing_updatesAndReturnsEntity() {
        Trainee existing = new Trainee("A","B","A.B","pw",true,
                LocalDate.of(2000,1,1),"old","ID1");
        when(traineeDAO.getTrainee("ID1")).thenReturn(existing);
        when(traineeDAO.existsByUsername("C.D")).thenReturn(false);
        when(traineeDAO.updateTrainee(eq("ID1"), any()))
                .thenAnswer(i -> i.getArgument(1)); // return updated entity

        TraineeDto dto = new TraineeDto();
        dto.setFirstname("C"); dto.setLastname("D");
        dto.setDateOfBirth(LocalDate.of(2001,2,2));
        dto.setAddress("newaddr");

        Trainee updated = service.updateTrainee("ID1", dto);
        assertNotNull(updated);
        assertEquals("C.D", updated.getUsername());
        assertEquals(dto.getDateOfBirth(), updated.getDateOfBirth());
        assertEquals(dto.getAddress(), updated.getAddress());
    }

    @Test
    void deleteTrainee_existing_returnsEntity() {
        Trainee existing = new Trainee("X","Y","X.Y","pw",true,
                LocalDate.now(),"addr","ID2");
        when(traineeDAO.deleteTrainee("ID2")).thenReturn(existing);

        Trainee removed = service.deleteTrainee("ID2");
        assertSame(existing, removed);
    }

    @Test
    void deleteTrainee_nonExisting_returnsNull() {
        when(traineeDAO.deleteTrainee("NOPE")).thenReturn(null);

        Trainee removed = service.deleteTrainee("NOPE");
        assertNull(removed);
    }
}
