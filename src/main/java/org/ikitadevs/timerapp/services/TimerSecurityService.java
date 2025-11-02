package org.ikitadevs.timerapp.services;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.exceptions.InvalidTimerOwnerException;
import org.ikitadevs.timerapp.repositories.TimerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Qualifier("timerSecurityService")
public class TimerSecurityService {
    private final TimerRepository timerRepository;


    public boolean isOwner(UUID uuidTimer, User currentUser) {
        return timerRepository.existsByUuidAndUser(uuidTimer, currentUser);
    }
}
