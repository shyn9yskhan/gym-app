package com.shyn9yskhan.gym_crm_system;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDTO;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDTO;
import com.shyn9yskhan.gym_crm_system.dto.TrainingDTO;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.Training;
import com.shyn9yskhan.gym_crm_system.service.TraineeService;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
import com.shyn9yskhan.gym_crm_system.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Facade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public Facade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    // Trainee service
    public Trainee createTrainee(TraineeDTO traineeDTO) {
        return traineeService.createTrainee(traineeDTO);
    }

    public boolean updateTrainee(String traineeId, TraineeDTO traineeDTO) {
        return traineeService.updateTrainee(traineeId, traineeDTO);
    }

    public boolean deleteTrainee(String traineeId) {
        return traineeService.deleteTrainee(traineeId);
    }

    public Trainee getTrainee(String traineeId) {
        return traineeService.getTrainee(traineeId);
    }


    //Trainer service
    public Trainer createTrainer(TrainerDTO trainerDTO) {
        return trainerService.createTrainer(trainerDTO);
    }

    public boolean updateTrainer(String trainerId, TrainerDTO trainerDTO) {
        return trainerService.updateTrainer(trainerId, trainerDTO);
    }

    public Trainer getTrainer(String trainerId) {
        return trainerService.getTrainer(trainerId);
    }


    //Training service
    public Training createTraining(TrainingDTO trainingDTO) {
        return trainingService.createTraining(trainingDTO);
    }

    public Training getTraining(String trainingId) {
        return trainingService.getTraining(trainingId);
    }
}