package org.ikitadevs.timerapp.mappers;


import org.ikitadevs.timerapp.dto.request.TimerCreateDto;
import org.ikitadevs.timerapp.dto.response.TimerResponseDto;
import org.ikitadevs.timerapp.entities.Timer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AvatarMapper.class})
public interface TimerMapper {
    Timer toEntity(TimerCreateDto timerCreateDto);

    @Mapping(source = "user.id", target = "user_id")
    TimerResponseDto toTimerResponseDto(Timer timer);
}
