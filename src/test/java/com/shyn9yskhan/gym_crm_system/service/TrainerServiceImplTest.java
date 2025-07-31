package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTrainerRepository;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import com.shyn9yskhan.gym_crm_system.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private InMemoryTrainerRepository inMemoryTrainerRepository;

    @InjectMocks
    private TrainerServiceImpl service;

    @Test
    void createTrainer_generatesUniqueUsernameAndPersists() {
        TrainerDto dto = new TrainerDto("Jane","Doe","CARDIO");

        when(inMemoryTrainerRepository.existsByUsername("Jane.Doe")).thenReturn(true);
        when(inMemoryTrainerRepository.findUsernamesByBase("Jane.Doe"))
                .thenReturn(Arrays.asList("Jane.Doe","Jane.Doe1"));
        when(inMemoryTrainerRepository.createTrainer(any())).thenAnswer(i -> i.getArgument(0));

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        Trainer created = service.createTrainer(dto);

        verify(inMemoryTrainerRepository).createTrainer(captor.capture());
        assertEquals("Jane.Doe2", captor.getValue().getUsername());
        assertEquals(new TrainingType("CARDIO"), captor.getValue().getSpecialization());
    }

    @Test
    void updateTrainer_nonExisting_returnsNull_andVerifiesGet() {
        when(inMemoryTrainerRepository.getTrainer("X")).thenReturn(null);

        Trainer result = service.updateTrainer("X", new TrainerDto());
        assertNull(result);
        verify(inMemoryTrainerRepository).getTrainer("X");
        verify(inMemoryTrainerRepository, never()).updateTrainer(any(), any());
    }

    @Test
    void updateTrainer_existing_updatesAndReturnsEntity_andVerifiesUpdate() {
        Trainer existing = new Trainer("A","B","A.B","pw",true,
                new TrainingType("YOGA"),"ID1");
        when(inMemoryTrainerRepository.getTrainer("ID1")).thenReturn(existing);
        when(inMemoryTrainerRepository.existsByUsername("C.D")).thenReturn(false);
        when(inMemoryTrainerRepository.updateTrainer(eq("ID1"), any()))
                .thenAnswer(i -> i.getArgument(1));

        TrainerDto dto = new TrainerDto("C","D","STRENGTH");
        Trainer updated = service.updateTrainer("ID1", dto);

        assertNotNull(updated);
        assertEquals("C.D", updated.getUsername());
        assertEquals(new TrainingType("STRENGTH"), updated.getSpecialization());

        verify(inMemoryTrainerRepository).getTrainer("ID1");
        verify(inMemoryTrainerRepository).updateTrainer(eq("ID1"), any(Trainer.class));
    }
}
