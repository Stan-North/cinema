package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveSessionRequest;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.service.SessionService;
import com.javaacademy.cinema.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Session controller")
@RequestMapping("/session")
public class SessionController {
    private final SessionService sessionService;
    private final ValidationService validationService;
    private static final String HEADER_NAME = "user-token";

    @PostMapping()
    @Operation(summary = "Создание сессии", description = "Принимает if фильма, дату и цену")
    @ApiResponse(responseCode = "201", description = "Успешное создание сессии",
            content = @Content(mediaType = "application.json",
                    schema = @Schema(implementation = Session.class)))
    @ApiResponse(responseCode = "403", description = "Неверный токен пользователя")
    @CacheEvict(value = "sessions", allEntries = true)
    public ResponseEntity<?> createSession(
            @Parameter(description = "Токен авторизации", required = true, example = "user-token <token>")
            @RequestHeader(value = HEADER_NAME) String userToken,
            @RequestBody SaveSessionRequest request) {
        validationService.validateUserToken(userToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.saveSession(request));
    }

    @GetMapping()
    @Cacheable(value = "sessions")
    @ApiResponse(responseCode = "200", description = "Показывает список сеансов с пагинацией",
            content = @Content(mediaType = "application.json",
                    schema = @Schema(implementation = PageDto.class)))
    public ResponseEntity<?> findAllSessions(
            @Parameter (description = "страница пагинации", required = true, example = "1")
            @RequestParam Integer pageNumber) {
        return ResponseEntity.ok().body(sessionService.getSessions(pageNumber));
    }

    @GetMapping("/{sessionId}/free-place")
    @Cacheable(value = "tickets")
    @ApiResponse(responseCode = "200", description = "Возвращает список свободных мест на сеансе. Принимает id сессии",
            content = @Content(array = @ArraySchema(schema = @Schema(example = "A1"))))
    public ResponseEntity<?> findFreeSeatsBySession(
            @Parameter (description = "id сеанса", required = true, example = "2")
            @PathVariable Integer sessionId) {
        return ResponseEntity.ok().body(sessionService.getFreeSeats(sessionId));
    }
}
