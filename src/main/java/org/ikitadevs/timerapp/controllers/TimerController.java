package org.ikitadevs.timerapp.controllers;


import com.fasterxml.jackson.annotation.JsonView;
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
public class TimerController {

    private final TimerService timerService;
    private final TimerMapper timerMapper;
    private final UserService userService;
    @GetMapping
    @JsonView(Views.User.class)
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
    public ResponseEntity<String> deleteUser(
            @PathVariable UUID uuid
    ) {
        timerService.deleteTimer(uuid);
        return ResponseEntity.ok("Timer was deleted!");
    }
    @GetMapping("/{uuid}")
    @JsonView(Views.User.class)
    public ResponseEntity<TimerResponseDto> getTimerInfo(@PathVariable UUID uuid, @AuthenticationPrincipal User currentUser) {
        Timer timer = timerService.getByUuid(uuid);
        TimerResponseDto timerResponseDto = timerMapper.toTimerResponseDto(timer);
        return ResponseEntity.ok(timerResponseDto);
    }


    @PatchMapping("/{uuid}")
    @JsonView(Views.User.class)
    public ResponseEntity<TimerResponseDto> updateTimer(
            @RequestBody TimerUpdateDto timerUpdateDto,
            @PathVariable UUID uuid) {
        TimerResponseDto timerResponseDto = timerService.updateTimer(uuid, timerUpdateDto);
        return ResponseEntity.ok(timerResponseDto);
    }

    @PatchMapping("/{uuid}/start")
    @JsonView(Views.User.class)
    public ResponseEntity<TimerResponseDto> startTimer(
            @PathVariable UUID uuid, @AuthenticationPrincipal User currentUser) {
        Timer timer = timerService.getByUuid(uuid);
        TimerResponseDto timerResponseDto = timerService.startTimer(uuid);
        return ResponseEntity.ok(timerResponseDto);
    }



    @PostMapping
    @JsonView(Views.User.class)
    public ResponseEntity<TimerResponseDto> createTimer(@RequestBody @Valid TimerCreateDto timerCreateDto, @AuthenticationPrincipal User user) {
        TimerResponseDto timerResponseDto = timerService.createTimer(user.getUuid(), timerCreateDto);
        return ResponseEntity.ok(timerResponseDto);
    }
}
