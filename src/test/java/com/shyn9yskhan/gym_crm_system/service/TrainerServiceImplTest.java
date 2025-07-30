package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTrainerRepository;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    @Mock
    private InMemoryTrainerRepository trainerDAO;

    @InjectMocks
    private TrainerServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_generatesUniqueUsernameAndPersists() {
        TrainerDto dto = new TrainerDto("Jane","Doe","CARDIO");

        when(trainerDAO.existsByUsername("Jane.Doe")).thenReturn(true);
        when(trainerDAO.findUsernamesByBase("Jane.Doe"))
                .thenReturn(Arrays.asList("Jane.Doe","Jane.Doe1"));
        when(trainerDAO.createTrainer(any())).thenAnswer(i -> i.getArgument(0));

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        Trainer created = service.createTrainer(dto);

        verify(trainerDAO).createTrainer(captor.capture());
        assertEquals("Jane.Doe2", captor.getValue().getUsername());
        assertEquals(new TrainingType("CARDIO"), captor.getValue().getSpecialization());
    }

    @Test
    void updateTrainer_nonExisting_returnsNull() {
        when(trainerDAO.getTrainer("X")).thenReturn(null);

        Trainer result = service.updateTrainer("X", new TrainerDto());
        assertNull(result);
    }

    @Test
    void updateTrainer_existing_updatesAndReturnsEntity() {
        Trainer existing = new Trainer("A","B","A.B","pw",true,
                new TrainingType("YOGA"),"ID1");
        when(trainerDAO.getTrainer("ID1")).thenReturn(existing);
        when(trainerDAO.existsByUsername("C.D")).thenReturn(false);
        when(trainerDAO.updateTrainer(eq("ID1"), any()))
                .thenAnswer(i -> i.getArgument(1)); // return updated entity

        TrainerDto dto = new TrainerDto("C","D","STRENGTH");
        Trainer updated = service.updateTrainer("ID1", dto);

        assertNotNull(updated);
        assertEquals("C.D", updated.getUsername());
        assertEquals(new TrainingType("STRENGTH"), updated.getSpecialization());
    }
}
