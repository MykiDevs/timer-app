package org.ikitadevs.timerapp.controllers;


import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.Views;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.dto.response.AvatarResponseDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.AvatarMapper;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.services.AvatarService;
import org.ikitadevs.timerapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Manage the profile of the currently authenticated user.")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AvatarService avatarService;
    private final AvatarMapper avatarMapper;

    @PatchMapping("/profile")
    @JsonView(Views.User.class)
    @Operation(summary = "Update current user data")
    public ResponseEntity<?> updateCurrentUser(
            @RequestBody @Valid UserPatchDto userPatchDto,
            @AuthenticationPrincipal User currentUser
            ) throws AccessDeniedException {
        userService.updateUser(userPatchDto, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    @JsonView(Views.User.class)
    @Operation(summary = "Get user profile")

    public ResponseEntity<UserResponseDto> getCurrentUser(
            @AuthenticationPrincipal User currentUser
    ) {
        User userProfile = userService.getByUuid(currentUser.getUuid());
        return ResponseEntity.ok(userMapper.toUserResponseDto(userProfile));
    }

    @DeleteMapping("/profile")
    @JsonView(Views.User.class)
    @Operation(summary = "Delete current user data")

    public ResponseEntity<String> deleteCurrentUser(
            @AuthenticationPrincipal User currentUser
    ) {
        userService.deleteCurrentUser(currentUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/profile/avatar")
    @JsonView(Views.User.class)
    @Operation(summary = "Delete current user avatar")
    public ResponseEntity<String> deleteCurrentUserAvatar(
            @AuthenticationPrincipal User currentUser
    ) {
        avatarService.deleteAvatarByUserUuid(currentUser.getUuid());
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/profile/avatar")
    @JsonView(Views.User.class)
    @Operation(summary = "Get current user avatar")
    public ResponseEntity<AvatarResponseDto> getCurrentUserAvatar(
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(avatarService.getAvatarByUserUuid(currentUser.getUuid()));
    }

    @PostMapping("/profile/avatar")
    @JsonView(Views.User.class)
    @Operation(summary = "Update current user avatar")
    public ResponseEntity<AvatarResponseDto> updateCurrentUserAvatar(
            @AuthenticationPrincipal User currentUser,
            @RequestPart MultipartFile avatar
    ) {
        return ResponseEntity.ok(avatarService.updateAvatarByUserUuid(avatar, currentUser.getUuid()));
    }
}
