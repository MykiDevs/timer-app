package org.ikitadevs.timerapp.mappers;

import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateDto userCreateDto);

    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "password", ignore = true)
    void UpdateUserFromDto(UserPatchDto dto, @MappingTarget User user);
}
