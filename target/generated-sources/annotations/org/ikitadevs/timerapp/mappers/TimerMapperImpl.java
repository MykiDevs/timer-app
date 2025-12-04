package org.ikitadevs.timerapp.mappers;

import javax.annotation.processing.Generated;
import org.ikitadevs.timerapp.dto.request.TimerCreateDto;
import org.ikitadevs.timerapp.dto.response.TimerResponseDto;
import org.ikitadevs.timerapp.entities.Timer;
import org.ikitadevs.timerapp.entities.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-17T20:10:36+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class TimerMapperImpl implements TimerMapper {

    @Override
    public Timer toEntity(TimerCreateDto timerCreateDto) {
        if ( timerCreateDto == null ) {
            return null;
        }

        Timer timer = new Timer();

        timer.setName( timerCreateDto.getName() );

        return timer;
    }

    @Override
    public TimerResponseDto toTimerResponseDto(Timer timer) {
        if ( timer == null ) {
            return null;
        }

        TimerResponseDto timerResponseDto = new TimerResponseDto();

        timerResponseDto.setUser_id( timerUserId( timer ) );
        timerResponseDto.setId( timer.getId() );
        timerResponseDto.setUuid( timer.getUuid() );
        timerResponseDto.setName( timer.getName() );
        timerResponseDto.setTimerState( timer.getTimerState() );
        timerResponseDto.setStartTime( timer.getStartTime() );
        timerResponseDto.setEndTime( timer.getEndTime() );
        timerResponseDto.setRemainingDuration( timer.getRemainingDuration() );
        timerResponseDto.setInitialDuration( timer.getInitialDuration() );

        return timerResponseDto;
    }

    private Long timerUserId(Timer timer) {
        if ( timer == null ) {
            return null;
        }
        User user = timer.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
