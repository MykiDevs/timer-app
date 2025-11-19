package org.ikitadevs.timerapp.controllers;


import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.Views;
import org.ikitadevs.timerapp.dto.request.PaginationRequestDto;
import org.ikitadevs.timerapp.dto.request.TimerCreateDto;
import org.ikitadevs.timerapp.dto.request.TimerUpdateDto;
import org.ikitadevs.timerapp.dto.response.PaginationResponseDto;
import org.ikitadevs.timerapp.dto.response.TimerResponseDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.Timer;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.TimerMapper;
import org.ikitadevs.timerapp.services.TimerService;
import org.ikitadevs.timerapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
    import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timers/")
@Tag(name = "Timers", description = "Manage timers for the currently authenticated user.")
public class TimerController {

    private final TimerService timerService;
    private final TimerMapper timerMapper;
    private final UserService userService;
    @GetMapping
    @JsonView(Views.User.class)
    @Operation(summary = "Get all timers belonging to current user")
    public ResponseEntity<PaginationResponseDto<List<TimerResponseDto>>> getAllTimersWithPaginationByUser(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PaginationRequestDto paginationRequestDto = new PaginationRequestDto(page, size, sortBy, sortDirection);
        PaginationResponseDto<List<TimerResponseDto>> paginationResponseDto = timerService.getAllTimersWithPaginationByUser(paginationRequestDto, currentUser.getUuid());
        return ResponseEntity.ok(paginationResponseDto);
    }

    @DeleteMapping("/{uuid}")
    @JsonView(Views.User.class)
    @Operation(summary = "Delete timer that belongs to current user")
    public ResponseEntity<String> deleteTimerOfCurrentUser(
            @PathVariable UUID uuid
    ) {
        timerService.deleteTimer(uuid);
        return ResponseEntity.ok("Timer was deleted!");
    }

    @GetMapping("/{uuid}")
    @JsonView(Views.User.class)
    @Operation(summary = "Get timer info that belongs to current user")
    public ResponseEntity<TimerResponseDto> getTimerInfo(@PathVariable UUID uuid, @AuthenticationPrincipal User currentUser) {
        Timer timer = timerService.getByUuidAndUser(uuid, currentUser);
        TimerResponseDto timerResponseDto = timerMapper.toTimerResponseDto(timer);
        return ResponseEntity.ok(timerResponseDto);
    }


    @PatchMapping("/{uuid}")
    @JsonView(Views.User.class)
    @Operation(summary = "Update timer that belongs to current user")
    public ResponseEntity<TimerResponseDto> updateTimer(
            @RequestBody TimerUpdateDto timerUpdateDto,
            @PathVariable UUID uuid) {
        TimerResponseDto timerResponseDto = timerService.updateTimer(uuid, timerUpdateDto);
        return ResponseEntity.ok(timerResponseDto);
    }

    @PatchMapping("/{uuid}/start")
    @JsonView(Views.User.class)
    @Operation(summary = "Start timer that belongs to current user")
    public ResponseEntity<TimerResponseDto> startTimer(
            @PathVariable UUID uuid) {
        Timer timer = timerService.getByUuid(uuid);
        TimerResponseDto timerResponseDto = timerService.startTimer(uuid);
        return ResponseEntity.ok(timerResponseDto);
    }

    @PatchMapping("/{uuid}/pause")
    @JsonView(Views.User.class)
    @Operation(summary = "Pause timer that belongs to current user")
    public ResponseEntity<TimerResponseDto> pauseTimer(
            @PathVariable UUID uuid) {
        Timer timer = timerService.getByUuid(uuid);
        TimerResponseDto timerResponseDto = timerService.pauseTimer(uuid);
        return ResponseEntity.ok(timerResponseDto);
    }

    @PatchMapping("/{uuid}/resume")
    @JsonView(Views.User.class)
    @Operation(summary = "Resume timer that belongs to current user")
    public ResponseEntity<TimerResponseDto> resumeTimer(
            @PathVariable UUID uuid) {
        Timer timer = timerService.getByUuid(uuid);
        TimerResponseDto timerResponseDto = timerService.resumeTimer(uuid);
        return ResponseEntity.ok(timerResponseDto);
    }

    @PatchMapping("/{uuid}/finish")
    @JsonView(Views.User.class)
    @Operation(summary = "Finish timer that belongs to current user")
    public ResponseEntity<TimerResponseDto> finishTimer(
            @PathVariable UUID uuid) {
        Timer timer = timerService.getByUuid(uuid);
        TimerResponseDto timerResponseDto = timerService.finishTimer(uuid);
        return ResponseEntity.ok(timerResponseDto);
    }

    @PostMapping
    @JsonView(Views.User.class)
    @Operation(summary = "Create timer")
    public ResponseEntity<TimerResponseDto> createTimer(@RequestBody @Valid TimerCreateDto timerCreateDto, @AuthenticationPrincipal User user) {
        TimerResponseDto timerResponseDto = timerService.createTimer(user.getUuid(), timerCreateDto);
        return ResponseEntity.ok(timerResponseDto);
    }
}
