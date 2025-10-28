package org.ikitadevs.timerapp.services;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.exceptions.InvalidTimerOwnerException;
import org.ikitadevs.timerapp.repositories.TimerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimerSecurityService {
    private final TimerRepository timerRepository;


    boolean isOwner(Long timerId, User currentUser) {
        Long currentUserId = currentUser.getId();
        boolean exists = timerRepository.existsByIdAndUser(timerId, currentUser);
        if(!exists) throw  new InvalidTimerOwnerException();
    }
}
