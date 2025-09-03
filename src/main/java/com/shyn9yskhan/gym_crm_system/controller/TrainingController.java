package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.AddTrainingRequest;
import com.shyn9yskhan.gym_crm_system.domain.Training;
import com.shyn9yskhan.gym_crm_system.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gym/training")
@Tag(name = "Training API", description = "Endpoints for managing trainings in the gym CRM system")
public class TrainingController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);
    private TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    @Operation(
            summary = "Add a new training",
            description = "Creates a new training session in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Void> addTraining(
            @RequestBody @Schema(description = "Request to add a new training") AddTrainingRequest addTrainingRequest
    ) {
        String createdTrainingId = trainingService.addTraining(addTrainingRequest);
        if (createdTrainingId != null) return new ResponseEntity<>(HttpStatus.CREATED);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{trainingId}")
    @Operation(
            summary = "Get training by ID",
            description = "Retrieves the training details by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Training found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Training.class))
            ),
            @ApiResponse(responseCode = "404", description = "Training not found")
    })
    public ResponseEntity<Training> getTraining(
            @Parameter(description = "ID of the training", required = true) @PathVariable String trainingId
    ) {
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
