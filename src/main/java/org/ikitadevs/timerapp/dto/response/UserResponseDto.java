package org.ikitadevs.timerapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.ikitadevs.timerapp.Views;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    @JsonView(Views.User.class)
    private UUID uuid;
    @JsonView(Views.Admin.class)
    private Long id;

    @JsonView(Views.User.class)
    private String name;
    @JsonView(Views.User.class)
    private String email;
    @JsonView(Views.User.class)
    private AvatarResponseDto avatar;
    @JsonView(Views.User.class)
    private String accessToken = null;
    @JsonView(Views.User.class)
    private String refreshToken = null;
}
