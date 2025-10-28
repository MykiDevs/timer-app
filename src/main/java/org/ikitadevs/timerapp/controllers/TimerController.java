package org.ikitadevs.timerapp.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.dto.request.PaginationRequestDto;
import org.ikitadevs.timerapp.dto.request.TimerCreateDto;
import org.ikitadevs.timerapp.dto.request.TimerUpdateDto;
import org.ikitadevs.timerapp.dto.response.PaginationResponseDto;
import org.ikitadevs.timerapp.dto.response.TimerResponseDto;
import org.ikitadevs.timerapp.entities.Timer;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.TimerMapper;
import org.ikitadevs.timerapp.services.TimerService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<?> getTimersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PaginationRequestDto paginationRequestDto = new PaginationRequestDto(page, size, sortBy, sortDirection);
        PaginationResponseDto<List<TimerResponseDto>> paginationResponseDto = timerService.getAllTimersWithPagination(paginationRequestDto);
        return ResponseEntity.ok(paginationResponseDto);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TimerResponseDto> getTimerInfo(@PathVariable UUID uuid) {
        Timer timer = timerService.getByUuidWithAvatar(uuid);
        return ResponseEntity.ok(timerMapper.toTimerResponseDto(timer));
    }


    @PatchMapping("/{uuid}")
    public ResponseEntity<TimerResponseDto> updateTimer(
            @RequestBody TimerUpdateDto timerUpdateDto,
            @PathVariable UUID uuid) {
        timerService.updateTimer(uuid, timerUpdateDto);
    }

    @PatchMapping("/{uuid}/start")
    public ResponseEntity<TimerResponseDto> startTimer(
            @PathVariable UUID uuid) {
        Timer timer = timerService.getByUuidWithAvatar(uuid);
        TimerResponseDto timerResponseDto = timerService.startTimer(uuid);
        return ResponseEntity.ok(timerResponseDto);

    }

    @PostMapping
    public ResponseEntity<TimerResponseDto> createTimer(@RequestBody @Valid TimerCreateDto timerCreateDto, @AuthenticationPrincipal User user) {
        TimerResponseDto timerResponseDto = timerService.createTimer(user.getUuid(), timerCreateDto);
        return ResponseEntity.ok(timerResponseDto);
    }
}
