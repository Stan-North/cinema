package com.javaacademy.cinema.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data integrity violation: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid arguments: " + e.getMessage());
    }

    @ExceptionHandler(SessionDoesNotExistException.class)
    public ResponseEntity<String> handleSessionDoesNotExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Сессия с таким id не существует");
    }

    @ExceptionHandler(TicketDoesNotExistException.class)
    public ResponseEntity<String> handleTicketDoesNotExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Билет с таким id не существует");
    }

    @ExceptionHandler(SeatDoesNotExistException.class)
    public ResponseEntity<String> handleSeatDoesNotExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Место с таким названием не существует");
    }

    @ExceptionHandler(TicketStatusException.class)
    public ResponseEntity<String> handleTicketStatusException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
    }

}
