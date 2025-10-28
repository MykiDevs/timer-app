package org.ikitadevs.timerapp.exceptions;

public class InvalidTimerOwnerException extends TimerException{
    public InvalidTimerOwnerException() {
        super("You haven't access to this timer!");
    }
}
