package org.ikitadevs.timerapp.controllers;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.Views;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PatchMapping("/profile")
    @JsonView(Views.User.class)
    public ResponseEntity<UserResponseDto> updateCurrentUser(
            @RequestBody @Valid UserPatchDto userPatchDto,
            @AuthenticationPrincipal User currentUser
            ) throws AccessDeniedException {
        UserResponseDto userResponseDto = userService.updateOwnProfile(userPatchDto, currentUser);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/profile")
    @JsonView(Views.User.class)
    public ResponseEntity<UserResponseDto> getCurrentUser(
            @AuthenticationPrincipal User currentUser
    ) {
        User userProfile = userService.getByUuid(currentUser.getUuid());
        return ResponseEntity.ok(userMapper.toUserResponseDto(userProfile));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteCurrentUer(
            @AuthenticationPrincipal User currentUser
    ) {
        userService.deleteUser(currentUser);
        return ResponseEntity.ok("You was removed!");
    }
}
