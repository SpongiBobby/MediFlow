package it.mediflow.sportello.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends MediFlowException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
