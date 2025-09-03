package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.dto.ChangePasswordRequest;
import com.shyn9yskhan.gym_crm_system.dto.LoginRequest;
import com.shyn9yskhan.gym_crm_system.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "Endpoints for user authentication and password management")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user with username and password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> login(
            @RequestBody @Schema(description = "Login credentials") LoginRequest loginRequest
    ) {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean ok = authenticationService.authenticate(loginRequest.username(), loginRequest.password());
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/change-password")
    @Operation(
            summary = "Change user password",
            description = "Changes the password for a user after verifying the old one."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or change failed")
    })
    public ResponseEntity<Void> changePassword(
            @RequestBody @Schema(description = "Request to change password") ChangePasswordRequest changePasswordRequest
    ) {
        String newPassword = authenticationService.changePassword(changePasswordRequest.username(), changePasswordRequest.oldPassword(), changePasswordRequest.newPassword());
        if (newPassword == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
