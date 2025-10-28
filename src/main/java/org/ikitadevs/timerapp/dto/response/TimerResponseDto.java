package org.ikitadevs.timerapp.dto.response;


import lombok.Data;
import org.ikitadevs.timerapp.entities.enums.TimerState;

import java.time.Duration;
import java.time.Instant;

@Data
public class TimerResponseDto {
    private String name;
    private TimerState timerState;
    private Instant startTime;
    private Instant endTime;
    private Duration remainingDuration;
    private Duration initialDuration;
}
