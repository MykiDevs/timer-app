package org.ikitadevs.timerapp.controllers;


import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.Views;
import org.ikitadevs.timerapp.dto.request.PaginationRequestDto;
import org.ikitadevs.timerapp.dto.response.PaginationResponseDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "User management (Admin)", description = "Administrative operations for managing all users.")
public class UserAdminController {
    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping
    @JsonView(Views.Admin.class)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Get all users using pagination")
    public ResponseEntity<PaginationResponseDto<List<UserResponseDto>>> getAllUsersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PaginationRequestDto paginationRequestDto = new PaginationRequestDto(page, size, sortBy, sortDirection);
        PaginationResponseDto<List<UserResponseDto>> paginationResponseDto = userService.getAllUsersWithPagination(paginationRequestDto);
        return ResponseEntity.ok(paginationResponseDto);

    }

    @GetMapping("/profile")
    @JsonView(Views.Admin.class)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Check admin's profile")
    public ResponseEntity<UserResponseDto> getProfileInfo(
            @AuthenticationPrincipal User currentUser
    ) {
        User user = userService.getByUuid(currentUser.getUuid());
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    @PatchMapping("/{uuid}")
    @JsonView(Views.Admin.class)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Get user info ")
    public ResponseEntity<UserResponseDto> getUserInfo(
            @PathVariable UUID uuid
            ) {
        User user = userService.getByUuid(uuid);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("/{uuid}")
    @JsonView(Views.Admin.class)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete user")
    public ResponseEntity<String> deleteUser(
            @PathVariable UUID uuid
    ) {
        userService.deleteUserByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

}
