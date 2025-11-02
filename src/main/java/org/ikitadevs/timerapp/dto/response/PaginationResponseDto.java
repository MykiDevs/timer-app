package org.ikitadevs.timerapp.dto.response;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.ikitadevs.timerapp.Views;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginationResponseDto<T> {
    @JsonView(Views.User.class)
    private T content;

    @JsonView(Views.User.class)
    @Min(value = 0, message = "Page minimum value is 0!")
    private int page;

    @JsonView(Views.User.class)
    @Size(min = 3, max = 10, message = "Size must be between 3 and 10!")
    private int size;

    @JsonView(Views.User.class)
    private String sortBy;
    @JsonView(Views.User.class)
    private String sortDirection;
    @JsonView(Views.User.class)
    private int totalElements;
    @JsonView(Views.User.class)
    private int totalPages;
}