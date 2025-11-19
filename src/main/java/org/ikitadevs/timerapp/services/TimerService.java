package org.ikitadevs.timerapp.services;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.dto.request.PaginationRequestDto;
import org.ikitadevs.timerapp.dto.request.TimerCreateDto;
import org.ikitadevs.timerapp.dto.request.TimerUpdateDto;
import org.ikitadevs.timerapp.dto.response.PaginationResponseDto;
import org.ikitadevs.timerapp.dto.response.TimerResponseDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.Timer;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.entities.enums.TimerState;
import org.ikitadevs.timerapp.exceptions.InvalidTimerTimeException;
import org.ikitadevs.timerapp.mappers.TimerMapper;
import org.ikitadevs.timerapp.repositories.TimerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerService {
    private final TimerMapper timerMapper;
    private final TimerRepository timerRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Timer getByUuid(UUID uuid) {
        return timerRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("Timer with this UUID doesn't exists!"));

    }

    @Transactional(readOnly = true)
    public Timer getByUuidAndUser(UUID uuid, User currentUser) {
        return timerRepository.findByUuidAndUser(uuid, currentUser).orElseThrow(() -> new EntityNotFoundException("Timer with this UUID doesn't exists!"));
    }

    @Transactional(readOnly = true)
    public PaginationResponseDto<List<TimerResponseDto>> getAllTimersWithPaginationByUser(PaginationRequestDto paginationRequestDto, UUID user_uuid) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequestDto.getSortDirection()), paginationRequestDto.getSortBy());
        Pageable pageable = PageRequest.of(paginationRequestDto.getPage(), paginationRequestDto.getSize(), sort);
        Page<Timer> timersPage = timerRepository.findAllByUser_Uuid(pageable, user_uuid);
        if(timersPage.getTotalPages() == 0) throw new EntityNotFoundException("This user has no timers");
        List<TimerResponseDto> timersList = timersPage.getContent().stream()
                .map(timerMapper::toTimerResponseDto)
                .toList();
        return new PaginationResponseDto<>(
                timersList,
                timersPage.getNumber(),
                timersPage.getSize(),
                paginationRequestDto.getSortBy(),
                paginationRequestDto.getSortDirection(),
                timersPage.getNumberOfElements(),
                timersPage.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public PaginationResponseDto<List<TimerResponseDto>> getAllTimersWithPagination(PaginationRequestDto paginationRequestDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequestDto.getSortDirection()), paginationRequestDto.getSortBy());
        Pageable pageable = PageRequest.of(paginationRequestDto.getPage(), paginationRequestDto.getSize(), sort);
        Page<Timer> timersPage = timerRepository.findAll(pageable);

        if(timersPage.getTotalPages() == 0) throw new EntityNotFoundException("There is no timers!");
        List<TimerResponseDto> timersList = timersPage.getContent().stream()
                .map(timerMapper::toTimerResponseDto)
                .toList();
        return new PaginationResponseDto<>(
                timersList,
                timersPage.getNumber(),
                timersPage.getSize(),
                paginationRequestDto.getSortBy(),
                paginationRequestDto.getSortDirection(),
                timersPage.getNumberOfElements(),
                timersPage.getTotalPages()
        );
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN') or @timerSecurityService.isOwner(#uuid, principal)")
    public TimerResponseDto updateTimer(UUID uuid, TimerUpdateDto timerUpdateDto) {
        Timer timer = getByUuid(uuid);
        return timerMapper.toTimerResponseDto(timer);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN') or @timerSecurityService.isOwner(#uuid, principal)")
    public TimerResponseDto startTimer(UUID uuid) {
        Timer timer = getByUuid(uuid);
        timer.start();
        timerRepository.save(timer);
        return timerMapper.toTimerResponseDto(timer);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN') or @timerSecurityService.isOwner(#uuid, principal)")
    public TimerResponseDto pauseTimer(UUID uuid) {
        Timer timer = getByUuid(uuid);
        timer.pause();
        timerRepository.save(timer);
        return timerMapper.toTimerResponseDto(timer);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN') or @timerSecurityService.isOwner(#uuid, principal)")
    public TimerResponseDto resumeTimer(UUID uuid) {
        Timer timer = getByUuid(uuid);
        timer.resume();
        timerRepository.save(timer);
        return timerMapper.toTimerResponseDto(timer);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN') or @timerSecurityService.isOwner(#uuid, principal)")
    public TimerResponseDto finishTimer(UUID uuid) {
        Timer timer = getByUuid(uuid);
        timer.finish();
        timerRepository.save(timer);
        return timerMapper.toTimerResponseDto(timer);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN') or @timerSecurityService.isOwner(#uuid, principal)")
    public void deleteTimer(UUID uuid) {
        timerRepository.deleteTimerByUuid(uuid);
    }

    @Transactional
    public TimerResponseDto createTimer(UUID uuid, TimerCreateDto timerCreateDto) {
        String name = timerCreateDto.getName();
        int hours = timerCreateDto.getHours();
        int minutes = timerCreateDto.getMinutes();
        int seconds = timerCreateDto.getSeconds();
        User user = userService.getByUuid(uuid);
        if(hours == 0 && minutes == 0 && seconds == 0) throw new InvalidTimerTimeException();
        Timer timer = new Timer(name, hours, minutes, seconds);
        log.info("Timer was created: {}", timer);
        timer.setUser(user);
        timerRepository.save(timer);
        return timerMapper.toTimerResponseDto(timer);
    }
}
