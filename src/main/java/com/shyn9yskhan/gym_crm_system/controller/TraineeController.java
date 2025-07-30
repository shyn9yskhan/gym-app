package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/gym/trainee")
public class TraineeController {

    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);
    private TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    public ResponseEntity<Trainee> createTrainee(@RequestBody TraineeDto traineeDto) {
        logger.info("Creating new trainee");
        Trainee created = traineeService.createTrainee(traineeDto);
        logger.info("Trainee created: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{traineeId}")
    public ResponseEntity<Void> updateTrainee(@PathVariable String traineeId, @RequestBody TraineeDto traineeDto) {
        logger.info("Updating trainee with ID: {}", traineeId);
        Trainee updatedTrainee = traineeService.updateTrainee(traineeId, traineeDto);
        if (updatedTrainee == null) {
            logger.warn("Trainee update failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            logger.info("Trainee updated successfully");
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{traineeId}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable String traineeId) {
        logger.info("Deleting trainee with ID: {}", traineeId);
        Trainee deletedTrainee = traineeService.deleteTrainee(traineeId);
        if (deletedTrainee == null) {
            logger.warn("Trainee deletion failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            logger.info("Trainee deleted successfully");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{traineeId}")
    public ResponseEntity<Trainee> getTrainee(@PathVariable String traineeId) {
        logger.info("Fetching trainee with ID: {}", traineeId);
        Trainee trainee = traineeService.getTrainee(traineeId);
        if (trainee != null) {
            logger.info("Trainee found");
            return ResponseEntity.ok(trainee);
        } else {
            logger.warn("Trainee not found");
            return ResponseEntity.notFound().build();
        }
    }
}
