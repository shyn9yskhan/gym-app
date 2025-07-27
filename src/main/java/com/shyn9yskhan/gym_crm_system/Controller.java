package com.shyn9yskhan.gym_crm_system;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDTO;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDTO;
import com.shyn9yskhan.gym_crm_system.dto.TrainingDTO;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gym")
public class Controller {

    private Facade facade;

    @Autowired
    public void setFacade(Facade facade) {
        this.facade = facade;
    }

    //Trainee controller
    @PostMapping("/trainee")
    public ResponseEntity<Trainee> createTrainee(@RequestBody TraineeDTO traineeDTO) {
        Trainee created = facade.createTrainee(traineeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/trainee/{traineeId}")
    public ResponseEntity<Void> updateTrainee(@PathVariable String traineeId, @RequestBody TraineeDTO traineeDTO) {
        boolean success = facade.updateTrainee(traineeId, traineeDTO);
        if (success) return ResponseEntity.noContent().build();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/trainee/{traineeId}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable String traineeId) {
        boolean success = facade.deleteTrainee(traineeId);
        if (success) return ResponseEntity.noContent().build();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/trainee/{traineeId}")
    public ResponseEntity<Trainee> getTrainee(@PathVariable String traineeId) {
        Trainee trainee = facade.getTrainee(traineeId);
        return trainee != null ? ResponseEntity.ok(trainee) : ResponseEntity.notFound().build();
    }


    //Trainer controller
    @PostMapping("/trainer")
    public ResponseEntity<Trainer> createTrainer(@RequestBody TrainerDTO trainerDTO) {
        Trainer created = facade.createTrainer(trainerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/trainer/{trainerId}")
    public ResponseEntity<Void> updateTrainer(@PathVariable String trainerId, @RequestBody TrainerDTO trainerDTO) {
        boolean success = facade.updateTrainer(trainerId, trainerDTO);
        if (success) return ResponseEntity.noContent().build();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<Trainer> getTrainer(@PathVariable String trainerId) {
        Trainer trainer = facade.getTrainer(trainerId);
        return trainer != null ? ResponseEntity.ok(trainer) : ResponseEntity.notFound().build();
    }


    //Training controller
    @PostMapping("/training")
    public ResponseEntity<Training> createTraining(@RequestBody TrainingDTO trainingDTO) {
        Training created = facade.createTraining(trainingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<Training> getTraining(@PathVariable String trainingId) {
        Training training = facade.getTraining(trainingId);
        return training != null ? ResponseEntity.ok(training) : ResponseEntity.notFound().build();
    }
}