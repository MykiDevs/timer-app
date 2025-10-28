package org.ikitadevs.timerapp.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginationRequestDto {
    @Min(value = 0, message = "Page minimum value is 0!")
    private int page;

    @Size(min = 3, max = 10, message = "Size must be between 3 and 10!")
    private int size;


    private String sortBy;
    private String sortDirection;
}
