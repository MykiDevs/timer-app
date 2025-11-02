package org.ikitadevs.timerapp.dto.response;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.ikitadevs.timerapp.Views;
import org.ikitadevs.timerapp.entities.enums.TimerState;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
public class TimerResponseDto {

    @JsonView(Views.Admin.class)
    private Long id;
    @JsonView(Views.Admin.class)
    private Long user_id;

    @JsonView(Views.User.class)
    private UUID uuid;
    @JsonView(Views.User.class)
    private String name;
    @JsonView(Views.User.class)
    private TimerState timerState;
    @JsonView(Views.User.class)
    private Instant startTime;
    @JsonView(Views.User.class)
    private Instant endTime;
    @JsonView(Views.User.class)
    private Duration remainingDuration;
    @JsonView(Views.User.class)
    private Duration initialDuration;


}
