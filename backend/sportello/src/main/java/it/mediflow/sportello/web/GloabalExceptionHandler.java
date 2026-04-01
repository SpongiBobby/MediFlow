package it.mediflow.sportello.web;

import it.mediflow.sportello.web.dto.ValidationErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GloabalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorResponseDto>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponseDto> errorMessage = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ValidationErrorResponseDto error = new ValidationErrorResponseDto();

            error.setField(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());

            errorMessage.add(error);
        }
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
