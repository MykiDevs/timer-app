package org.ikitadevs.timerapp.services;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.dto.request.PaginationRequestDto;
import org.ikitadevs.timerapp.dto.request.TimerCreateDto;
import org.ikitadevs.timerapp.dto.request.TimerUpdateDto;
import org.ikitadevs.timerapp.dto.response.PaginationResponseDto;
import org.ikitadevs.timerapp.dto.response.TimerResponseDto;
import org.ikitadevs.timerapp.entities.Timer;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.exceptions.InvalidTimerTimeException;
import org.ikitadevs.timerapp.mappers.TimerMapper;
import org.ikitadevs.timerapp.repositories.TimerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimerService {
    private final TimerMapper timerMapper;
    private final TimerRepository timerRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Timer getByUuid(UUID uuid) {
        return timerRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("Timer with this UUID doesn't exists!"));

    }


    @Transactional(readOnly = true)
    public Timer getByUuidWithAvatar(UUID uuid) {
        return timerRepository.findByUuidWithAvatar(uuid).orElseThrow(() -> new EntityNotFoundException("Timer with this UUID doesn't exists!"));
    }

    @Transactional(readOnly = true)
    public Timer getByUuidAndUserWithAvatar(UUID uuid, User currentUser) {
        return timerRepository.findByUuidAndUserWithAvatar(uuid, currentUser).orElseThrow(() -> new EntityNotFoundException("Timer with this UUID doesn't exists!"));

    }

    @Transactional(readOnly = true)
    public PaginationResponseDto<List<TimerResponseDto>> getAllTimersWithPagination(PaginationRequestDto paginationRequestDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequestDto.getSortDirection()), paginationRequestDto.getSortBy());
        Pageable pageable = PageRequest.of(paginationRequestDto.getPage(), paginationRequestDto.getSize(), sort);
        Page<Timer> timerPage = timerRepository.findAll(pageable);
        List<TimerResponseDto> timerList = timerPage.getContent().stream()
                .map(timerMapper::toTimerResponseDto)
                .toList();
        PaginationResponseDto<List<TimerResponseDto>> paginationResponseDto = new PaginationResponseDto<>();
        paginationResponseDto.setContent(timerList);
        paginationResponseDto.setPage(timerPage.getNumber());
        paginationResponseDto.setSize(timerPage.getSize());
        paginationResponseDto.setTotalElements(timerPage.getNumberOfElements());
        paginationResponseDto.setTotalPages(timerPage.getTotalPages());
        return paginationResponseDto;
    }

    @Transactional
    public TimerResponseDto updateTimer(UUID uuid, TimerUpdateDto timerUpdateDto) {
        Timer timer = getByUuid(uuid);
        return timerMapper.toTimerResponseDto(timer);
    }

    @Transactional
    public TimerResponseDto startTimer(UUID uuid) {
        Timer timer = getByUuidWithAvatar(uuid);
        timer.start();
        timerRepository.save(timer);
        return timerMapper.toTimerResponseDto(timer);
    }

    @Transactional
    public TimerResponseDto createTimer(UUID uuid, TimerCreateDto timerCreateDto) {
        String name = timerCreateDto.getName();
        Long hours = timerCreateDto.getHours();
        Long minutes = timerCreateDto.getMinutes();
        Long seconds = timerCreateDto.getSeconds();
        User user = userService.getByUuid(uuid);
        if(hours == 0L && minutes == 0L && seconds == 0L) throw new InvalidTimerTimeException();
        Timer timer = new Timer(name, hours, minutes, seconds);
        timer.setUser(user);
        timerRepository.save(timer);
        return timerMapper.toTimerResponseDto(timer);
    }


}
