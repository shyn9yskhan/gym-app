package com.shyn9yskhan.gym_crm_system.controller;

import com.shyn9yskhan.gym_crm_system.security.BruteForceService;
import com.shyn9yskhan.gym_crm_system.security.JwtUtil;
import com.shyn9yskhan.gym_crm_system.security.TokenBlacklist;
import com.shyn9yskhan.gym_crm_system.dto.ChangePasswordRequest;
import com.shyn9yskhan.gym_crm_system.dto.LoginRequest;
import com.shyn9yskhan.gym_crm_system.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "Endpoints for user authentication and password management")
public class AuthenticationController {
    private AuthenticationService authenticationService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private TokenBlacklist tokenBlacklist;
    private BruteForceService bruteForceService;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, TokenBlacklist tokenBlacklist, BruteForceService bruteForceService) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklist = tokenBlacklist;
        this.bruteForceService = bruteForceService;
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
    public ResponseEntity<?> login(
            @RequestBody @Schema(description = "Login credentials") LoginRequest loginRequest
    ) {
        String username = loginRequest.username();
        if (bruteForceService.isBlocked(username)) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("User blocked due to failed attempts");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequest.password())
            );
            bruteForceService.recordSuccess(username);
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (BadCredentialsException ex) {
            bruteForceService.recordFailure(username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            Instant expiration = jwtUtil.getExpirationInstant(token);
            tokenBlacklist.blacklist(token);
        }
        return ResponseEntity.ok().build();
    }
}
