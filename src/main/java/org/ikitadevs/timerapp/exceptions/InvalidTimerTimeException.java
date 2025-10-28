package org.ikitadevs.timerapp.exceptions;

public class InvalidTimerTimeException extends TimerException {
    public InvalidTimerTimeException() {
        super("Timer duration is invalid");
    }
}
