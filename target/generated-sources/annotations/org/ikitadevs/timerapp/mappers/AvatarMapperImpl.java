package org.ikitadevs.timerapp.mappers;

import java.util.UUID;
import javax.annotation.processing.Generated;
import org.ikitadevs.timerapp.dto.response.AvatarResponseDto;
import org.ikitadevs.timerapp.entities.Avatar;
import org.ikitadevs.timerapp.entities.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-17T20:10:36+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class AvatarMapperImpl implements AvatarMapper {

    @Override
    public AvatarResponseDto toAvatarResponseDto(Avatar avatar) {
        if ( avatar == null ) {
            return null;
        }

        AvatarResponseDto avatarResponseDto = new AvatarResponseDto();

        avatarResponseDto.setUser_uuid( avatarUserUuid( avatar ) );
        avatarResponseDto.setId( avatar.getId() );
        avatarResponseDto.setPath( avatar.getPath() );

        return avatarResponseDto;
    }

    private UUID avatarUserUuid(Avatar avatar) {
        if ( avatar == null ) {
            return null;
        }
        User user = avatar.getUser();
        if ( user == null ) {
            return null;
        }
        UUID uuid = user.getUuid();
        if ( uuid == null ) {
            return null;
        }
        return uuid;
    }
}
