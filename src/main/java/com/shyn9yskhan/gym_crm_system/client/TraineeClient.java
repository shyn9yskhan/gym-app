package com.shyn9yskhan.gym_crm_system.client;

import com.shyn9yskhan.gym_crm_system.dto.UserDto;
import com.shyn9yskhan.gym_crm_system.dto.GetTraineeTrainingsListRequest;
import com.shyn9yskhan.gym_crm_system.dto.GetTraineeTrainingsListResponse;
import com.shyn9yskhan.gym_crm_system.dto.GetTrainerTrainingsListRequest;
import com.shyn9yskhan.gym_crm_system.dto.GetTrainerTrainingsListResponse;
import com.shyn9yskhan.gym_crm_system.entity.TrainerEntity;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
import com.shyn9yskhan.gym_crm_system.service.TrainingService;
import com.shyn9yskhan.gym_crm_system.service.UserCreationResult;
import com.shyn9yskhan.gym_crm_system.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeClient {

    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;

    public TraineeClient(TrainerService trainerService,
                         TrainingService trainingService,
                         UserService userService) {
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.userService = userService;
    }

    public UserCreationResult userService_createUser(String firstname, String lastname) {
        return userService.createUser(firstname, lastname);
    }

    public UserDto userService_updateUser(UserDto userDto) {
        return userService.updateUser(userDto);
    }

    public String userService_deleteUser(String username) {
        return userService.deleteUser(username);
    }

    public String userService_setActiveByUsername(String username, boolean isActive) {
        return userService.setActiveByUsername(username, isActive);
    }

    public List<TrainerEntity> trainerService_findAllByUserUsernameIn(List<String> usernames) {
        return trainerService.findAllByUserUsernameIn(usernames);
    }

    public List<GetTraineeTrainingsListResponse> trainingService_getTraineeTrainingsList(
            GetTraineeTrainingsListRequest request) {
        return trainingService.getTraineeTrainingsList(request);
    }

    public List<GetTrainerTrainingsListResponse> trainingService_getTrainerTrainingsList(
            GetTrainerTrainingsListRequest request) {
        return trainingService.getTrainerTrainingsList(request);
    }
}
