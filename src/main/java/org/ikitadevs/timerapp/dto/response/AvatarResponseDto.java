package org.ikitadevs.timerapp.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.ikitadevs.timerapp.Views;

import java.util.UUID;

@Data
public class AvatarResponseDto {

    @JsonView(Views.Admin.class)
    private Long id;

    @JsonView(Views.User.class)
    private String path;

    @JsonView(Views.User.class)
    private UUID user_uuid;
}
