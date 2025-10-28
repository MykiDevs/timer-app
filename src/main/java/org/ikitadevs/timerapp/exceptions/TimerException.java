package org.ikitadevs.timerapp.exceptions;

public abstract class TimerException extends RuntimeException{
    TimerException(String message) {
        super(message);
    }
}
