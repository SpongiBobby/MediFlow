package it.mediflow.sportello.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends MediFlowException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
