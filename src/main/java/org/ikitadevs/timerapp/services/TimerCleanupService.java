package org.ikitadevs.timerapp.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.entities.enums.TimerState;
import org.ikitadevs.timerapp.repositories.TimerRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerCleanupService {
    private final TimerRepository timerRepository;

    @Scheduled(fixedRate = 1800000)
    public void cleanupFinishedTimers() {
        int deletedTimersCount = timerRepository.deleteByTimerState(TimerState.FINISHED);
        log.info("{} timers was cleaned!", deletedTimersCount);
    }
}
