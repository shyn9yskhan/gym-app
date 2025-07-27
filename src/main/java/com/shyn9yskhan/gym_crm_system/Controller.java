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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/gym")
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private Facade facade;

    @Autowired
    public void setFacade(Facade facade) {
        this.facade = facade;
    }

    //Trainee controller
    @PostMapping("/trainee")
    public ResponseEntity<Trainee> createTrainee(@RequestBody TraineeDTO traineeDTO) {
        logger.info("Creating new trainee");
        Trainee created = facade.createTrainee(traineeDTO);
        logger.info("Trainee created: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/trainee/{traineeId}")
    public ResponseEntity<Void> updateTrainee(@PathVariable String traineeId, @RequestBody TraineeDTO traineeDTO) {
        logger.info("Updating trainee with ID: {}", traineeId);
        boolean success = facade.updateTrainee(traineeId, traineeDTO);
        if (success) {
            logger.info("Trainee updated successfully");
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Trainee update failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/trainee/{traineeId}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable String traineeId) {
        logger.info("Deleting trainee with ID: {}", traineeId);
        boolean success = facade.deleteTrainee(traineeId);
        if (success) {
            logger.info("Trainee deleted successfully");
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Trainee deletion failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/trainee/{traineeId}")
    public ResponseEntity<Trainee> getTrainee(@PathVariable String traineeId) {
        logger.info("Fetching trainee with ID: {}", traineeId);
        Trainee trainee = facade.getTrainee(traineeId);
        if (trainee != null) {
            logger.info("Trainee found");
            return ResponseEntity.ok(trainee);
        } else {
            logger.warn("Trainee not found");
            return ResponseEntity.notFound().build();
        }
    }


    //Trainer controller
    @PostMapping("/trainer")
    public ResponseEntity<Trainer> createTrainer(@RequestBody TrainerDTO trainerDTO) {
        logger.info("Creating new trainer");
        Trainer created = facade.createTrainer(trainerDTO);
        logger.info("Trainer created: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/trainer/{trainerId}")
    public ResponseEntity<Void> updateTrainer(@PathVariable String trainerId, @RequestBody TrainerDTO trainerDTO) {
        logger.info("Updating trainer with ID: {}", trainerId);
        boolean success = facade.updateTrainer(trainerId, trainerDTO);
        if (success) {
            logger.info("Trainer updated successfully");
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Trainer update failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<Trainer> getTrainer(@PathVariable String trainerId) {
        logger.info("Fetching trainer with ID: {}", trainerId);
        Trainer trainer = facade.getTrainer(trainerId);
        if (trainer != null) {
            logger.info("Trainer found");
            return ResponseEntity.ok(trainer);
        } else {
            logger.warn("Trainer not found");
            return ResponseEntity.notFound().build();
        }
    }


    //Training controller
    @PostMapping("/training")
    public ResponseEntity<Training> createTraining(@RequestBody TrainingDTO trainingDTO) {
        logger.info("Creating new training");
        Training created = facade.createTraining(trainingDTO);
        logger.info("Training created: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<Training> getTraining(@PathVariable String trainingId) {
        logger.info("Fetching training with ID: {}", trainingId);
        Training training = facade.getTraining(trainingId);
        if (training != null) {
            logger.info("Training found");
            return ResponseEntity.ok(training);
        } else {
            logger.warn("Training not found");
            return ResponseEntity.notFound().build();
        }
    }
}