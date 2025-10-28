package org.ikitadevs.timerapp.dto.response;

import lombok.Data;

@Data
public class AvatarResponseDto {
    private Long id;
    private String path;
    private UserResponseDto user;
}
