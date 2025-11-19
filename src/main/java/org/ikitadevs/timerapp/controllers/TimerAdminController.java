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
@RequestMapping("/api/admin/timers/")
@Tag(name = "Timer Management (Admin)", description = "Administrative operations for managing all timers in the system.")
public class TimerAdminController {

    private final TimerService timerService;
    private final TimerMapper timerMapper;
    private final UserService userService;

    @GetMapping
    @JsonView(Views.Admin.class)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Get all timers using pagination")
    public ResponseEntity<?> getAllTimersWithPagination(
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
    @JsonView(Views.Admin.class)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Get timer info by uuid")
    public ResponseEntity<TimerResponseDto> getTimerInfo(@PathVariable UUID uuid) {
        Timer timer = timerService.getByUuid(uuid);
        TimerResponseDto timerResponseDto = timerMapper.toTimerResponseDto(timer);
        return ResponseEntity.ok(timerResponseDto);
    }

    @PatchMapping("/{uuid}")
    @JsonView(Views.Admin.class)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update timer by uuid")

    public ResponseEntity<TimerResponseDto> updateTimer(@PathVariable UUID uuid,
                                                        @RequestBody TimerUpdateDto timerUpdateDto) {
        TimerResponseDto timerResponseDto = timerService.updateTimer(uuid, timerUpdateDto);
        return ResponseEntity.ok(timerResponseDto);
    }

}
