package com.javaacademy.cinema.exception;

import com.javaacademy.cinema.common.ErrorMessages;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.DATA_INTEGRITY_VIOLATION
                + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.INVALID_ARGUMENTS + e.getMessage());
    }

    @ExceptionHandler(SessionDoesNotExistException.class)
    public ResponseEntity<String> handleSessionDoesNotExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.SESSION_DOES_NOT_EXIST);
    }

    @ExceptionHandler(TicketDoesNotExistException.class)
    public ResponseEntity<String> handleTicketDoesNotExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.TICKET_DOES_NOT_EXIST);
    }

    @ExceptionHandler(SeatDoesNotExistException.class)
    public ResponseEntity<String> handleSeatDoesNotExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.SEAT_DOES_NOT_EXIST);
    }

    @ExceptionHandler(TicketStatusException.class)
    public ResponseEntity<String> handleTicketStatusException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.UNEXPECTED_ERROR
                + e.getMessage());
    }

}
