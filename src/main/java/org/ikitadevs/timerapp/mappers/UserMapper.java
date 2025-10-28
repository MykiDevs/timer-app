package org.ikitadevs.timerapp.mappers;

import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.mappers.context.CycleAvoidingMappingContext;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
uses = {AvatarMapper.class})
public interface UserMapper {
    User toEntity(UserCreateDto userCreateDto);

    UserResponseDto toUserResponseDto(User user, @Context CycleAvoidingMappingContext context);

    @Mapping(target = "password", ignore = true)
    void UpdateUserFromDto(UserPatchDto dto, @MappingTarget User user);
}
