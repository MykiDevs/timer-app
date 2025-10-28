package org.ikitadevs.timerapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private String name;
    private String email;
    private AvatarResponseDto avatar;
    private String accessToken = null;
    private String refreshToken = null;
}
