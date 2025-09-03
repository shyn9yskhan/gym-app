package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.*;
import com.shyn9yskhan.gym_crm_system.domain.Trainee;
import com.shyn9yskhan.gym_crm_system.service.TraineeService;
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
@RequestMapping("/gym/trainee")
@Tag(name = "Trainee API", description = "Endpoints for managing trainees in the gym CRM system")
public class TraineeController {

    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new trainee",
            description = "Registers a new trainee in the system and returns the created profile with credentials."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Trainee created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateTraineeResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<CreateTraineeResponse> createTrainee(
            @RequestBody @Schema(description = "Trainee details for creation") TraineeDto traineeDto
    ) {
        logger.info("Creating new trainee");
        CreateTraineeResponse createTraineeResponse = traineeService.createTrainee(traineeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTraineeResponse);
    }

    @PutMapping
    @Operation(
            summary = "Update an existing trainee",
            description = "Updates the trainee's profile based on the provided request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or update failed")
    })
    public ResponseEntity<Void> updateTrainee(
            @RequestBody @Schema(description = "Request to update trainee details") UpdateTraineeRequest updateTraineeRequest
    ) {
        String username = updateTraineeRequest.getUsername();
        logger.info("Updating trainee with username: {}", username);
        TraineeProfile updatedTraineeProfile = traineeService.updateTrainee(updateTraineeRequest);
        if (updatedTraineeProfile == null) {
            logger.warn("Trainee update failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            logger.info("Trainee updated successfully");
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{username}")
    @Operation(
            summary = "Delete a trainee",
            description = "Deletes the trainee by username."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Deletion failed")
    })
    public ResponseEntity<Void> deleteTrainee(
            @Parameter(description = "Username of the trainee to delete", required = true) @PathVariable String username
    ) {
        logger.info("Deleting trainee with username: {}", username);
        String deletedTraineeUsername = traineeService.deleteTrainee(username);
        if (deletedTraineeUsername == null) {
            logger.warn("Trainee deletion failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            logger.info("Trainee deleted successfully");
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{traineeId}")
    @Operation(
            summary = "Get trainee by ID",
            description = "Retrieves the trainee details by their ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainee found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Trainee.class))
            ),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public ResponseEntity<Trainee> getTrainee(
            @Parameter(description = "ID of the trainee", required = true) @PathVariable String traineeId
    ) {
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

    @GetMapping("/{username}")
    @Operation(
            summary = "Get trainee profile by username",
            description = "Retrieves the trainee's profile by username."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainee profile found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TraineeProfile.class))
            ),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public ResponseEntity<TraineeProfile> getTraineeProfile(
            @Parameter(description = "Username of the trainee", required = true) @PathVariable String username
    ) {
        TraineeProfile traineeProfile = traineeService.getTraineeProfileByUsername(username);
        if (traineeProfile != null) {
            return ResponseEntity.ok(traineeProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/trainers")
    @Operation(
            summary = "Update trainee's trainer list",
            description = "Updates the list of trainers assigned to the trainee."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer list updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrainerProfileDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<List<TrainerProfileDto>> updateTraineesTrainerList(
            @RequestBody @Schema(description = "Request to update trainer list") UpdateTraineesTrainerListRequest updateTraineesTrainerListRequest
    ) {
        return ResponseEntity.ok(traineeService.updateTraineesTrainerList(updateTraineesTrainerListRequest));
    }

    @GetMapping("/trainings")
    @Operation(
            summary = "Get trainee's trainings list",
            description = "Retrieves the list of trainings for the trainee, optionally filtered."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of trainings retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTraineeTrainingsListResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<List<GetTraineeTrainingsListResponse>> getTraineesTrainingsList(
            @RequestBody @Schema(description = "Request with filters for trainings") GetTraineeTrainingsListRequest getTraineeTrainingsListRequest
    ) {
        return ResponseEntity.ok(traineeService.getTraineeTrainingsList(getTraineeTrainingsListRequest));
    }

    @PatchMapping("/{username}/activation")
    @Operation(
            summary = "Update trainee activation status",
            description = "Activates or deactivates the trainee by username."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activation status updated"),
            @ApiResponse(responseCode = "400", description = "Update failed")
    })
    public ResponseEntity<Void> updateTraineeActivation(
            @Parameter(description = "Username of the trainee", required = true) @PathVariable String username,
            @Parameter(description = "Activation status (true to activate, false to deactivate)", required = true) @RequestParam boolean isActive
    ) {
        String result = traineeService.updateTraineeActivation(username, isActive);
        if (result != null) return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
