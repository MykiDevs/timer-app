package org.ikitadevs.timerapp.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.request.UserLoginDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.mappers.context.CycleAvoidingMappingContext;
import org.ikitadevs.timerapp.services.JwtService;
import org.ikitadevs.timerapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto userLoginDto) {
        UserResponseDto userResponseDto = userService.loginUser(userLoginDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserResponseDto userResponseDto = userService.createUser(userCreateDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshUser(@RequestBody Map<String, String> body) {
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    private ResponseEntity<?> getResponseEntity(User user, UserResponseDto userResponseDto) {
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        userResponseDto.setAccessToken(newAccessToken);
        userResponseDto.setRefreshToken(newRefreshToken);
        return ResponseEntity.ok(userResponseDto);
    }

}
