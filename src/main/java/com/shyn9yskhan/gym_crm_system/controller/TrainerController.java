package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.TrainerDto;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
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
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/gym/trainer")
public class TrainerController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
    private TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public ResponseEntity<Trainer> createTrainer(@RequestBody TrainerDto trainerDto) {
        logger.info("Creating new trainer");
        Trainer created = trainerService.createTrainer(trainerDto);
        logger.info("Trainer created: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<Void> updateTrainer(@PathVariable String trainerId, @RequestBody TrainerDto trainerDto) {
        logger.info("Updating trainer with ID: {}", trainerId);
        Trainer updatedTrainer = trainerService.updateTrainer(trainerId, trainerDto);
        if (updatedTrainer == null) {
            logger.warn("Trainer update failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            logger.info("Trainer updated successfully");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<Trainer> getTrainer(@PathVariable String trainerId) {
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
}
