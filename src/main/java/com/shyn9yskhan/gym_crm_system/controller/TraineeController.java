package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.TraineeDto;
import com.shyn9yskhan.gym_crm_system.domain.Trainee;
import com.shyn9yskhan.gym_crm_system.dto.UpdateTraineeRequest;
import com.shyn9yskhan.gym_crm_system.service.AuthenticationService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/gym/trainee")
public class TraineeController {

    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);
    private TraineeService traineeService;
    private AuthenticationService authenticationService;

    public TraineeController(TraineeService traineeService, AuthenticationService authenticationService) {
        this.traineeService = traineeService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<String> createTrainee(@RequestBody TraineeDto traineeDto) {
        logger.info("Creating new trainee");
        String createdTraineeId = traineeService.createTrainee(traineeDto);
        logger.info("Trainee created: {}", createdTraineeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTraineeId);
    }

    @PutMapping("/{traineeId}")
    public ResponseEntity<Void> updateTrainee(@PathVariable String traineeId, @RequestHeader("X-Username") String athUsername, @RequestHeader("X-Password") String athPassword, @RequestBody UpdateTraineeRequest request) {
        if (authenticationService.authenticate(athUsername, athPassword)) {
            logger.info("Updating trainee with ID: {}", traineeId);
            String updatedTraineeId = traineeService.updateTrainee(traineeId, request.getDateOfBirth(), request.getAddress());
            if (updatedTraineeId == null) {
                logger.warn("Trainee update failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                logger.info("Trainee updated successfully");
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/{traineeId}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable String traineeId, @RequestHeader("X-Username") String athUsername, @RequestHeader("X-Password") String athPassword) {
        if (authenticationService.authenticate(athUsername, athPassword)) {
            logger.info("Deleting trainee with ID: {}", traineeId);
            String deletedTraineeId = traineeService.deleteTrainee(traineeId);
            if (deletedTraineeId == null) {
                logger.warn("Trainee deletion failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                logger.info("Trainee deleted successfully");
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{traineeId}")
    public ResponseEntity<Trainee> getTrainee(@PathVariable String traineeId, @RequestHeader("X-Username") String athUsername, @RequestHeader("X-Password") String athPassword) {
        if (authenticationService.authenticate(athUsername, athPassword)) {
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
