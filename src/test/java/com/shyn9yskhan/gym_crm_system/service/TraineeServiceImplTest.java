package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.repository.impl.InMemoryTraineeRepository;
import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private InMemoryTraineeRepository inMemoryTraineeRepository;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_generatesUniqueUsernameAndPersists() {
        TraineeDto dto = new TraineeDto("John","Doe",LocalDate.of(1990,1,1),"Addr");

        when(inMemoryTraineeRepository.existsByUsername("John.Doe")).thenReturn(true);
        when(inMemoryTraineeRepository.findUsernamesByBase("John.Doe"))
                .thenReturn(Arrays.asList("John.Doe","John.Doe1","John.Doe2"));

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        when(inMemoryTraineeRepository.createTrainee(any())).thenAnswer(i -> i.getArgument(0));

        Trainee created = service.createTrainee(dto);

        verify(inMemoryTraineeRepository).createTrainee(captor.capture());
        Trainee persisted = captor.getValue();
        assertEquals("John.Doe3", persisted.getUsername());
        assertNotNull(persisted.getUserId());
        assertEquals(dto.getDateOfBirth(), persisted.getDateOfBirth());
        assertEquals(dto.getAddress(), persisted.getAddress());
        assertSame(created, persisted);
    }

    @Test
    void updateTrainee_nonExisting_returnsNull_andVerifiesGet() {
        when(inMemoryTraineeRepository.getTrainee("X")).thenReturn(null);

        Trainee result = service.updateTrainee("X", new TraineeDto());
        assertNull(result);
        verify(inMemoryTraineeRepository).getTrainee("X");
        verify(inMemoryTraineeRepository, never()).updateTrainee(any(), any());
    }

    @Test
    void updateTrainee_existing_updatesAndReturnsEntity_andVerifiesUpdate() {
        Trainee existing = new Trainee("A","B","A.B","pw",true,
                LocalDate.of(2000,1,1),"old","ID1");
        when(inMemoryTraineeRepository.getTrainee("ID1")).thenReturn(existing);
        when(inMemoryTraineeRepository.existsByUsername("C.D")).thenReturn(false);
        when(inMemoryTraineeRepository.updateTrainee(eq("ID1"), any()))
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

        verify(inMemoryTraineeRepository).getTrainee("ID1");
        verify(inMemoryTraineeRepository).updateTrainee(eq("ID1"), any(Trainee.class));
    }

    @Test
    void deleteTrainee_existing_returnsEntity_andVerifiesDelete() {
        Trainee existing = new Trainee("X","Y","X.Y","pw",true,
                LocalDate.now(),"addr","ID2");
        when(inMemoryTraineeRepository.deleteTrainee("ID2")).thenReturn(existing);

        Trainee removed = service.deleteTrainee("ID2");
        assertSame(existing, removed);
        verify(inMemoryTraineeRepository).deleteTrainee("ID2");
    }

    @Test
    void deleteTrainee_nonExisting_returnsNull_andVerifiesDelete() {
        when(inMemoryTraineeRepository.deleteTrainee("NOPE")).thenReturn(null);

        Trainee removed = service.deleteTrainee("NOPE");
        assertNull(removed);
        verify(inMemoryTraineeRepository).deleteTrainee("NOPE");
    }
}
