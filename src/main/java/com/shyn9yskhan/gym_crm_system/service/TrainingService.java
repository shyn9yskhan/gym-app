package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dto.*;
import com.shyn9yskhan.gym_crm_system.domain.Training;

import java.util.List;

public interface TrainingService {
    String addTraining(AddTrainingRequest addTrainingRequest);
    Training getTraining(String trainingId);
    List<GetTraineeTrainingsListResponse> getTraineeTrainingsList(GetTraineeTrainingsListRequest getTraineeTrainingsListRequest);
    List<GetTrainerTrainingsListResponse> getTrainerTrainingsList(GetTrainerTrainingsListRequest getTrainerTrainingsListRequest);
}
