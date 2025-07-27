package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dao.TrainerDAO;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDTO;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_generatesUniqueUsernameAndPersists() {
        TrainerDTO dto = new TrainerDTO();
        dto.setFirstname("Jane");
        dto.setLastname("Doe");
        dto.setTrainingTypeName("CARDIO");

        when(trainerDAO.existsByUsername("Jane.Doe")).thenReturn(true);
        when(trainerDAO.findUsernamesByBase("Jane.Doe")).thenReturn(Arrays.asList("Jane.Doe", "Jane.Doe1"));
        when(trainerDAO.createTrainer(any())).thenAnswer(i -> i.getArgument(0));

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        Trainer created = service.createTrainer(dto);

        verify(trainerDAO).createTrainer(captor.capture());
        assertEquals("Jane.Doe2", captor.getValue().getUsername());
        assertEquals(TrainingType.valueOf("CARDIO"), captor.getValue().getSpecialization());
    }

    @Test
    void updateTrainer_nonExisting_returnsFalse() {
        when(trainerDAO.getTrainer("X")).thenReturn(null);
        assertFalse(service.updateTrainer("X", new TrainerDTO()));
    }

    @Test
    void updateTrainer_existing_updatesValues() {
        Trainer existing = new Trainer("A","B","A.B","pw",true,
                new TrainingType("YOGA"),"ID1");
        when(trainerDAO.getTrainer("ID1")).thenReturn(existing);
        when(trainerDAO.existsByUsername("C.D")).thenReturn(false);
        when(trainerDAO.updateTrainer(eq("ID1"), any())).thenReturn(true);

        TrainerDTO dto = new TrainerDTO();
        dto.setFirstname("C"); dto.setLastname("D"); dto.setTrainingTypeName("STRENGTH");

        boolean result = service.updateTrainer("ID1", dto);
        assertTrue(result);
        assertEquals("C.D", existing.getUsername());
        assertEquals(TrainingType.valueOf("STRENGTH"), existing.getSpecialization());
    }
}