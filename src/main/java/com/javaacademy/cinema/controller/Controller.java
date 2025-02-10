package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.common.ErrorMessages;
import com.javaacademy.cinema.config.AppConfig;
import com.javaacademy.cinema.dto.*;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.service.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Cinema Controller")
public class Controller {
    private final CinemaService cinemaService;
    private final AppConfig appConfig;
    private static final String HEADER_NAME = "user-token";

    @PostMapping("/movie")
    @Operation(summary = "Создание фильма", description = "Принимает имя и описание фильма, " +
            "возвращает фильм с генерированным id")
    @ApiResponse(responseCode = "201", description = "Успешное добавление фильма")
    @ApiResponse(responseCode = "403", description = "Неверный токен пользователя")
    @CacheEvict(value = "movies", allEntries = true)
    public ResponseEntity<?> saveMovie(
            @RequestHeader(value = HEADER_NAME) String userToken,
            @RequestBody SaveMovieRequest request) {
        if (Boolean.FALSE.equals(isTokenCorrect(userToken))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.INVALID_USER_TOKEN);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cinemaService.saveMovie(request));
    }


    @PostMapping("/session")
    @Operation(summary = "Создание сессии", description = "Принимает фильм, дату и цену")
    @ApiResponse(responseCode = "201", description = "Успешное создание сессии")
    @ApiResponse(responseCode = "403", description = "Неверный токен пользователя")
    @CacheEvict(value = "sessions", allEntries = true)
    public ResponseEntity<?> createSession(
            @RequestHeader (value = HEADER_NAME) String userToken,
            @RequestBody SaveSessionRequest request) {
        if (Boolean.FALSE.equals(isTokenCorrect(userToken))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.INVALID_USER_TOKEN);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cinemaService.saveSession(request));
    }


    @GetMapping("/ticket/saled/{sessionId}")
    public ResponseEntity<?> getBoughtTickets(
            @RequestHeader (value = HEADER_NAME) String userToken,
            @PathVariable Integer sessionId) {
        if (Boolean.FALSE.equals(isTokenCorrect(userToken))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.INVALID_USER_TOKEN);
        }
        return ResponseEntity.ok(cinemaService.findBoughtTickets(sessionId));
    }

    @GetMapping("/movie")
    @Cacheable(value = "movies")
    public ResponseEntity<?> findAllMovies(@RequestParam Integer page) {
        return ResponseEntity.ok().body(cinemaService.getMoviePageDto(page));
    }

    @GetMapping("/session")
    @Cacheable(value = "sessions")
    public ResponseEntity<?> findAllSessions(@RequestParam Integer page) {
        return ResponseEntity.ok().body(cinemaService.getSessionPageDto(page));
    }

    @GetMapping("/session/{sessionId}/free-place")
    @Cacheable(value = "tickets")
    public ResponseEntity<?> findFreeSeatsBySession(@PathVariable Integer sessionId) {
        return ResponseEntity.ok().body(cinemaService.getFreeTickets(sessionId));
    }

    @PostMapping("ticket/booking")
    @CacheEvict(value = "tickets", allEntries = true)
    public ResponseEntity<?> buyTicket(@RequestBody BuyTicketRequest request) {
        return ResponseEntity.ok().body(cinemaService.buyTicket(request));
    }

    private Boolean isTokenCorrect(String headerValue) {
        String adminToken = appConfig.appProperties().getHeaderValue();
        return adminToken.equals(headerValue);
    }

}
