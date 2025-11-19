package org.ikitadevs.timerapp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TimerCreateDto {

    @NotNull
    private String name;

    @Min(value = 0, message = "Hours can be 0 or higher!")
    @Max(value = 120, message = "Hours can be 120 or lower!")
    private int hours = 0;

    @Min(value = 0, message = "Minutes can be 0 or higher!")
    @Max(value = 60, message = "Minutes can be 60 or lower!")
    private int minutes = 0;

    @Min(value = 0, message = "Seconds can be 0 or higher!")
    @Max(value = 60, message = "Seconds can be 60 or lower!")
    private int seconds = 0;
}
