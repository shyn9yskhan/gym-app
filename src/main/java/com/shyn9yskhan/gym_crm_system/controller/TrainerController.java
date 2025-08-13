package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.domain.Trainer;
import com.shyn9yskhan.gym_crm_system.dto.UpdateTrainerRequest;
import com.shyn9yskhan.gym_crm_system.service.AuthenticationService;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
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

@RestController
@RequestMapping("/gym/trainer")
public class TrainerController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
    private TrainerService trainerService;
    private AuthenticationService authenticationService;

    public TrainerController(TrainerService trainerService, AuthenticationService authenticationService) {
        this.trainerService = trainerService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<String> createTrainer(@RequestBody TrainerDto trainerDto) {
        logger.info("Creating new trainer");
        String createdTrainerId = trainerService.createTrainer(trainerDto);
        logger.info("Trainer created: {}", createdTrainerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrainerId);
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<Void> updateTrainer(@PathVariable String trainerId, @RequestHeader("X-Username") String athUsername, @RequestHeader("X-Password") String athPassword, @RequestBody UpdateTrainerRequest request) {
        if (authenticationService.authenticate(athUsername, athPassword)) {
            logger.info("Updating trainer with ID: {}", trainerId);
            String updatedTrainerId = trainerService.updateTrainer(trainerId, request.getTrainingTypeName());
            if (updatedTrainerId == null) {
                logger.warn("Trainer update failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                logger.info("Trainer updated successfully");
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<Trainer> getTrainer(@PathVariable String trainerId, @RequestHeader("X-Username") String athUsername, @RequestHeader("X-Password") String athPassword) {
        if (authenticationService.authenticate(athUsername, athPassword)) {
            logger.info("Fetching trainer with ID: {}", trainerId);
            Trainer trainer = trainerService.getTrainer(trainerId);
            if (trainer != null) {
                logger.info("Trainer found");
                return ResponseEntity.ok(trainer);
            } else {
                logger.warn("Trainer not found");
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
