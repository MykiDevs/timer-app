package org.ikitadevs.timerapp.controllers;


import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.Views;
import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.request.UserLoginDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.services.JwtService;
import org.ikitadevs.timerapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Authentication", description = "Simple JWT based REST API authentication controller for operating user data")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @PostMapping("/login")
    @JsonView(Views.Public.class)
    @Operation(summary = "Login with email and password", description = "Login with email and password. The response is User that has same credentials.")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto userLoginDto
    ) {
        isUserAlreadyAuthenticated();
        UserResponseDto userResponseDto = userService.loginUser(userLoginDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/signup")
    @JsonView(Views.Public.class)
    @Operation(summary = "Signup with email, name and password")
    public ResponseEntity<UserResponseDto> signupUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        isUserAlreadyAuthenticated();
        UserResponseDto userResponseDto = userService.createUser(userCreateDto);
        return ResponseEntity.created(URI.create("/api/users/" + userResponseDto.getUuid())).body(userResponseDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh user accessToken and refreshToken")
    public ResponseEntity<?> refreshUserToken(@RequestBody Map<String, String> body) {
        String oldRefreshToken = body.get("refreshToken");
        if(oldRefreshToken == null || oldRefreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is missing");
        }
        try {
            UUID userId = jwtService.extractUuid(oldRefreshToken);
            User user = userService.getByUuid(userId);
            if(jwtService.isTokenValidForUser(oldRefreshToken, user)) {
                String newAccessToken = jwtService.generateAccessToken(user);
                Map<String, String> response = new HashMap<>();
                response.put("accessToken", newAccessToken);
                response.put("refreshToken", jwtService.generateRefreshToken(user));
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("Failed to refresh token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    private void isUserAlreadyAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            throw new AccessDeniedException("You are already logged in! Log out first!");
        }
    }
}