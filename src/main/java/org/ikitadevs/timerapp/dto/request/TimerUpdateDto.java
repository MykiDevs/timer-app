package org.ikitadevs.timerapp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TimerUpdateDto {

    @NotNull
    private String name;

    @Min(value = 0, message = "Hours can't be lower than 0")
    private Long hours = 0L;

    @Min(value = 0, message = "Minutes can't be lower than 0")
    private Long minutes = 0L;

    @Min(value = 0, message = "Seconds can't be lower than 0")
    private Long seconds = 0L;
}
