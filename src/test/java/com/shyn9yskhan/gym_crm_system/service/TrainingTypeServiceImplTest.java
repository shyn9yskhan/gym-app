package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.entity.TrainingTypeEntity;
import com.shyn9yskhan.gym_crm_system.repository.TrainingTypeRepository;
import com.shyn9yskhan.gym_crm_system.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl service;

    @Test
    void getTrainingTypeByName_found_returnsEntity() {
        String name = "CARDIO";
        TrainingTypeEntity entity = new TrainingTypeEntity();
        entity.setId("tt-1");
        entity.setTrainingTypeName(name);
        when(trainingTypeRepository.findByTrainingTypeName(name)).thenReturn(Optional.of(entity));
        TrainingTypeEntity result = service.getTrainingTypeByName(name);
        assertNotNull(result);
        assertSame(entity, result);
        verify(trainingTypeRepository).findByTrainingTypeName(name);
    }

    @Test
    void getTrainingTypeByName_notFound_returnsNull() {
        String name = "UNKNOWN";
        when(trainingTypeRepository.findByTrainingTypeName(name)).thenReturn(Optional.empty());
        TrainingTypeEntity result = service.getTrainingTypeByName(name);
        assertNull(result);
        verify(trainingTypeRepository).findByTrainingTypeName(name);
    }
}
