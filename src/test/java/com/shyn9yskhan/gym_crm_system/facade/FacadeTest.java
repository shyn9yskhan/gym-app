package com.shyn9yskhan.gym_crm_system.facade;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDTO;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDTO;
import com.shyn9yskhan.gym_crm_system.dto.TrainingDTO;
import com.shyn9yskhan.gym_crm_system.Facade;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.service.TraineeService;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
import com.shyn9yskhan.gym_crm_system.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private Facade facade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_delegatesToService() {
        TraineeDTO dto = new TraineeDTO();
        Trainee expected = new Trainee();
        when(traineeService.createTrainee(dto)).thenReturn(expected);

        Trainee actual = facade.createTrainee(dto);
        assertSame(expected, actual);
        verify(traineeService).createTrainee(dto);
    }

    @Test
    void updateTrainee_delegatesToService() {
        when(traineeService.updateTrainee("id", null)).thenReturn(true);
        assertTrue(facade.updateTrainee("id", null));
        verify(traineeService).updateTrainee("id", null);
    }

    @Test
    void deleteTrainee_delegatesToService() {
        when(traineeService.deleteTrainee("id")).thenReturn(true);
        assertTrue(facade.deleteTrainee("id"));
        verify(traineeService).deleteTrainee("id");
    }

    @Test
    void getTrainee_delegatesToService() {
        Trainee expected = new Trainee();
        when(traineeService.getTrainee("id")).thenReturn(expected);
        assertSame(expected, facade.getTrainee("id"));
        verify(traineeService).getTrainee("id");
    }

    @Test
    void createTrainer_delegatesToService() {
        TrainerDTO dto = new TrainerDTO();
        Trainer expected = new Trainer();
        when(trainerService.createTrainer(dto)).thenReturn(expected);

        Trainer actual = facade.createTrainer(dto);
        assertSame(expected, actual);
        verify(trainerService).createTrainer(dto);
    }

    @Test
    void updateTrainer_delegatesToService() {
        when(trainerService.updateTrainer("id", null)).thenReturn(false);
        assertFalse(facade.updateTrainer("id", null));
        verify(trainerService).updateTrainer("id", null);
    }

    @Test
    void getTrainer_delegatesToService() {
        Trainer expected = new Trainer();
        when(trainerService.getTrainer("id")).thenReturn(expected);
        assertSame(expected, facade.getTrainer("id"));
        verify(trainerService).getTrainer("id");
    }

    @Test
    void createTraining_delegatesToService() {
        TrainingDTO dto = new TrainingDTO();
        Training expected = new Training();
        when(trainingService.createTraining(dto)).thenReturn(expected);

        Training actual = facade.createTraining(dto);
        assertSame(expected, actual);
        verify(trainingService).createTraining(dto);
    }

    @Test
    void getTraining_delegatesToService() {
        Training expected = new Training();
        when(trainingService.getTraining("id")).thenReturn(expected);
        assertSame(expected, facade.getTraining("id"));
        verify(trainingService).getTraining("id");
    }
}