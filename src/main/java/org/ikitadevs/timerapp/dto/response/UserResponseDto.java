package org.ikitadevs.timerapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ikitadevs.timerapp.Views;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    @JsonView(Views.Public.class)
    private UUID uuid;
    @JsonView(Views.Admin.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String name;
    @JsonView(Views.Public.class)
    private String email;
    @JsonView(Views.Public.class)
    private AvatarResponseDto avatar;
    @JsonView(Views.Public.class)
    private String accessToken = null;
    @JsonView(Views.Public.class)
    private String refreshToken = null;
}
