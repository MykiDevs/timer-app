package org.ikitadevs.timerapp.dto.response;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PaginationResponseDto<T> {
    private T content;

    @Min(value = 0, message = "Page minimum value is 0!")
    private int page;

    @Size(min = 3, max = 10, message = "Size must be between 3 and 10!")
    private int size;


    private String sortBy;
    private String sortDirection;
    private int totalElements;
    private int totalPages;
}