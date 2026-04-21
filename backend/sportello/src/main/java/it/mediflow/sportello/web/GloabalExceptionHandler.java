package it.mediflow.sportello.web;

import it.mediflow.sportello.exceptions.MediFlowException;
import it.mediflow.sportello.web.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GloabalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("[400] Validazione fallita su {}: {}", request.getRequestURI(), message);
        return ResponseEntity
                .badRequest()
                .body(ErrorResponseDto.of(HttpStatus.BAD_REQUEST, message, request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityException(DataIntegrityViolationException ex, HttpServletRequest request) {

        log.warn("[509] Validazione fallita su {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponseDto.of(HttpStatus.CONFLICT, "Operazione non riuscita: i dati inseriti non sono validi o sono già presenti.", request));
    }

    @ExceptionHandler(MediFlowException.class)
    public ResponseEntity<ErrorResponseDto> handleMediFlowError(MediFlowException ex, HttpServletRequest request) {
        log.error("[{}] {}: {}", ex.getStatus(), request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorResponseDto.of(ex.getStatus(), ex.getMessage(), request));
    }
}
