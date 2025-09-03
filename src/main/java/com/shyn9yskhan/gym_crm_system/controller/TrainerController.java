package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.*;
import com.shyn9yskhan.gym_crm_system.service.TrainerService;
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

import java.util.List;

@RestController
@RequestMapping("/gym/trainer")
@Tag(name = "Trainer API", description = "Endpoints for managing trainers in the gym CRM system")
public class TrainerController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
    private TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new trainer",
            description = "Registers a new trainer in the system and returns the created profile with credentials."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Trainer created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateTrainerResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<CreateTrainerResponse> createTrainer(
            @RequestBody @Schema(description = "Trainer details for creation") TrainerDto trainerDto
    ) {
        logger.info("Creating new trainer");
        CreateTrainerResponse createTrainerResponse = trainerService.createTrainer(trainerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTrainerResponse);
    }

    @PutMapping
    @Operation(
            summary = "Update an existing trainer",
            description = "Updates the trainer's profile based on the provided request."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Trainer updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerProfile.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request or update failed")
    })
    public ResponseEntity<TrainerProfile> updateTrainer(
            @RequestBody @Schema(description = "Request to update trainer details") UpdateTrainerRequest updateTrainerRequest
    ) {
        TrainerProfile updatedTrainerProfile = trainerService.updateTrainer(updateTrainerRequest);
        if (updatedTrainerProfile == null) {
            logger.warn("Trainer update failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            logger.info("Trainer updated successfully");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Get trainer profile by username",
            description = "Retrieves the trainer's profile by username."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer profile found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerProfile.class))
            ),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public ResponseEntity<TrainerProfile> getTrainer(
            @Parameter(description = "Username of the trainer", required = true) @PathVariable String username
    ) {
        logger.info("Fetching trainer with username: {}", username);
        TrainerProfile trainerProfile = trainerService.getTrainerProfileByUsername(username);
        if (trainerProfile != null) {
            logger.info("Trainer found");
            return ResponseEntity.ok(trainerProfile);
        } else {
            logger.warn("Trainer not found");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}/not-assigned-on-trainee-active-trainers")
    @Operation(
            summary = "Get active trainers not assigned to a trainee",
            description = "Retrieves a list of active trainers not assigned to the specified trainee by username."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of available trainers",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerProfileDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Trainee or trainers not found")
    })
    public ResponseEntity<List<TrainerProfileDto>> getNotAssignedOnTraineeActiveTrainers(
            @Parameter(description = "Username of the trainee", required = true) @PathVariable String username
    ) {
        return ResponseEntity.ok(trainerService.getNotAssignedOnTraineeActiveTrainers(username));
    }

    @GetMapping("/trainings")
    @Operation(
            summary = "Get trainer's trainings list",
            description = "Retrieves the list of trainings for the trainer, optionally filtered."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of trainings retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTrainerTrainingsListResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<List<GetTrainerTrainingsListResponse>> getTrainerTrainingsList(
            @RequestBody @Schema(description = "Request with filters for trainings") GetTrainerTrainingsListRequest getTrainerTrainingsListRequest
    ) {
        return ResponseEntity.ok(trainerService.getTrainerTrainings(getTrainerTrainingsListRequest));
    }

    @PatchMapping("/{username}/activation")
    @Operation(
            summary = "Update trainer activation status",
            description = "Activates or deactivates the trainer by username."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activation status updated"),
            @ApiResponse(responseCode = "400", description = "Update failed")
    })
    public ResponseEntity<Void> updateTrainerActivation(
            @Parameter(description = "Username of the trainer", required = true) @PathVariable String username,
            @Parameter(description = "Activation status (true to activate, false to deactivate)", required = true) @RequestParam boolean isActive
    ) {
        String result = trainerService.updateTrainerActivation(username, isActive);
        if (result != null) return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
