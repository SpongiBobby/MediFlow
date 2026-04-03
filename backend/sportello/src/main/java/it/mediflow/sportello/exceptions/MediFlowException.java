package it.mediflow.sportello.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MediFlowException extends RuntimeException {
    private final HttpStatus status;

    public MediFlowException(String message, HttpStatus status) {

        super(message);
        this.status = status;
    }
}
