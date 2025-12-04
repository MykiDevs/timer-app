package org.ikitadevs.timerapp.mappers;

import javax.annotation.processing.Generated;
import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.dto.response.AvatarResponseDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.Avatar;
import org.ikitadevs.timerapp.entities.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-17T20:10:36+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreateDto userCreateDto) {
        if ( userCreateDto == null ) {
            return null;
        }

        User user = new User();

        user.setName( userCreateDto.getName() );
        user.setPassword( userCreateDto.getPassword() );
        user.setEmail( userCreateDto.getEmail() );

        return user;
    }

    @Override
    public UserResponseDto toUserResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setUuid( user.getUuid() );
        userResponseDto.setId( user.getId() );
        userResponseDto.setName( user.getName() );
        userResponseDto.setEmail( user.getEmail() );
        userResponseDto.setAvatar( avatarToAvatarResponseDto( user.getAvatar() ) );

        return userResponseDto;
    }

    @Override
    public void UpdateUserFromDto(UserPatchDto dto, User user) {
        if ( dto == null ) {
            return;
        }

        user.setName( dto.getName() );
        user.setEmail( dto.getEmail() );
    }

    protected AvatarResponseDto avatarToAvatarResponseDto(Avatar avatar) {
        if ( avatar == null ) {
            return null;
        }

        AvatarResponseDto avatarResponseDto = new AvatarResponseDto();

        avatarResponseDto.setId( avatar.getId() );
        avatarResponseDto.setPath( avatar.getPath() );

        return avatarResponseDto;
    }
}
