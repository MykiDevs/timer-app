package org.ikitadevs.timerapp.mappers;


import org.ikitadevs.timerapp.dto.request.TimerCreateDto;
import org.ikitadevs.timerapp.dto.response.TimerResponseDto;
import org.ikitadevs.timerapp.entities.Timer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AvatarMapper.class})
public interface TimerMapper {
    Timer toEntity(TimerCreateDto timerCreateDto);

    TimerResponseDto toTimerResponseDto(Timer timer);
}
