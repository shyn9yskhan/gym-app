package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.TrainingDto;
import com.shyn9yskhan.gym_crm_system.domain.Training;
import com.shyn9yskhan.gym_crm_system.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/gym/training")
public class TrainingController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);
    private TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<String> createTraining(@RequestBody TrainingDto trainingDto) {
        logger.info("Creating new training");
        String createdTrainingId = trainingService.createTraining(trainingDto);
        logger.info("Training created: {}", createdTrainingId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrainingId);
    }

    @GetMapping("/{trainingId}")
    public ResponseEntity<Training> getTraining(@PathVariable String trainingId) {
        logger.info("Fetching training with ID: {}", trainingId);
        Training training = trainingService.getTraining(trainingId);
        if (training != null) {
            logger.info("Training found");
            return ResponseEntity.ok(training);
        } else {
            logger.warn("Training not found");
            return ResponseEntity.notFound().build();
        }
    }
}
