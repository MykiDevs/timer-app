package org.ikitadevs.timerapp.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.dto.request.UserLoginDto;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.mappers.context.CycleAvoidingMappingContext;
import org.ikitadevs.timerapp.services.UserService;
import org.mapstruct.control.MappingControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserPatchDto userPatchDto,
            @AuthenticationPrincipal User currentUser
            ) {
        User updatedUser = userService.updateUser(userPatchDto, id);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(updatedUser, new CycleAvoidingMappingContext());
        return ResponseEntity.ok(userResponseDto);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(userMapper.toUserResponseDto(currentUser, new CycleAvoidingMappingContext()));
    }
}
