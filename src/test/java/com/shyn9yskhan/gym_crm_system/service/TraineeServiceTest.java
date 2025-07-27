package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dao.TraineeDAO;
import com.shyn9yskhan.gym_crm_system.dto.TraineeDTO;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @InjectMocks
    private TraineeService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_generatesUniqueUsernameAndPersists() {
        // given
        TraineeDTO dto = new TraineeDTO("John", "Doe", LocalDate.of(1990,1,1), "Addr");

        // simulate existing base collision
        when(traineeDAO.existsByUsername("John.Doe")).thenReturn(true);
        when(traineeDAO.findUsernamesByBase("John.Doe") )
                .thenReturn(Arrays.asList("John.Doe", "John.Doe1", "John.Doe2"));

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        when(traineeDAO.createTrainee(any())).thenAnswer(i -> i.getArgument(0));

        // when
        Trainee created = service.createTrainee(dto);

        // then
        verify(traineeDAO).createTrainee(captor.capture());
        Trainee persisted = captor.getValue();
        assertEquals("John.Doe3", persisted.getUsername());
        assertNotNull(persisted.getUserId());
        assertEquals(dto.getDateOfBirth(), persisted.getDateOfBirth());
        assertEquals(dto.getAddress(), persisted.getAddress());
        assertSame(created, persisted);
    }

    @Test
    void updateTrainee_nonExisting_returnsFalse() {
        when(traineeDAO.getTrainee("X")).thenReturn(null);
        boolean result = service.updateTrainee("X", new TraineeDTO());
        assertFalse(result);
        verify(traineeDAO, times(1)).getTrainee("X");
    }

    @Test
    void updateTrainee_existing_updatesFields() {
        Trainee existing = new Trainee("A","B","A.B","pw",true,
                LocalDate.of(2000,1,1),"old","ID1");
        when(traineeDAO.getTrainee("ID1")).thenReturn(existing);
        when(traineeDAO.existsByUsername("C.D")).thenReturn(false);
        when(traineeDAO.updateTrainee(eq("ID1"), any())).thenReturn(true);

        TraineeDTO dto = new TraineeDTO();
        dto.setFirstname("C"); dto.setLastname("D");
        dto.setDateOfBirth(LocalDate.of(2001,2,2));
        dto.setAddress("newaddr");

        boolean result = service.updateTrainee("ID1", dto);
        assertTrue(result);
        assertEquals("C.D", existing.getUsername());
        assertEquals(dto.getDateOfBirth(), existing.getDateOfBirth());
        assertEquals(dto.getAddress(), existing.getAddress());
    }
}