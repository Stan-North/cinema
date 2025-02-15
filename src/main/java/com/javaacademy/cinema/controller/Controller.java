package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.common.ErrorMessages;
import com.javaacademy.cinema.config.AppConfig;
import com.javaacademy.cinema.dto.BuyTicketRequest;
import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveMovieRequest;
import com.javaacademy.cinema.dto.SaveSessionRequest;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.service.CinemaService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "Cinema controller")
public class Controller {
    private final CinemaService cinemaService;
    private final AppConfig appConfig;
    private static final String HEADER_NAME = "user-token";

    @PostMapping("/movie")
    @Operation(summary = "Создание фильма", description = "Принимает имя и описание фильма, "
            + "возвращает фильм с генерированным id")
    @ApiResponse(responseCode = "201", description = "Успешное добавление фильма",
        content = @Content(mediaType = "application.json",
        schema = @Schema(implementation = Movie.class)))
    @ApiResponse(responseCode = "403", description = "Неверный токен пользователя")
    @CacheEvict(value = "movies", allEntries = true)
    public ResponseEntity<?> saveMovie(
            @Parameter(description = "Токен авторизации", required = true, example = "user-token <token>")
            @RequestHeader(value = HEADER_NAME) String userToken,
            @RequestBody SaveMovieRequest request) {
        if (Boolean.FALSE.equals(isTokenCorrect(userToken))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.INVALID_USER_TOKEN);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cinemaService.saveMovie(request));
    }


    @PostMapping("/session")
    @Operation(summary = "Создание сессии", description = "Принимает if фильма, дату и цену")
    @ApiResponse(responseCode = "201", description = "Успешное создание сессии",
        content = @Content(mediaType = "application.json",
        schema = @Schema(implementation = Session.class)))
    @ApiResponse(responseCode = "403", description = "Неверный токен пользователя")
    @CacheEvict(value = "sessions", allEntries = true)
    public ResponseEntity<?> createSession(
            @Parameter(description = "Токен авторизации", required = true, example = "user-token <token>")
            @RequestHeader (value = HEADER_NAME) String userToken,
            @RequestBody SaveSessionRequest request) {
        if (Boolean.FALSE.equals(isTokenCorrect(userToken))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.INVALID_USER_TOKEN);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cinemaService.saveSession(request));
    }


    @GetMapping("/ticket/saled/{sessionId}")
    @Operation(summary = "Получение купленных билетов на сеанс", description = "должен быть указан id сеанса в пути + "
            + "Должен быть заголовок админа с токеном")
    @ApiResponse(responseCode = "200", description = "Возвращает список купленных билетов на указанный сеанс",
        content = @Content(mediaType = "application.json",
        array = @ArraySchema(schema = @Schema(implementation = Ticket.class))))
    @ApiResponse(responseCode = "403", description = "Неверный токен пользователя")
    public ResponseEntity<?> getBoughtTickets(
            @Parameter(description = "Токен авторизации", required = true, example = "user-token <token>")
            @RequestHeader (value = HEADER_NAME) String userToken,
            @Parameter (description = "id сеанса", required = true, example = "1")
            @PathVariable Integer sessionId) {
        if (Boolean.FALSE.equals(isTokenCorrect(userToken))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.INVALID_USER_TOKEN);
        }
        return ResponseEntity.ok(cinemaService.findBoughtTickets(sessionId));
    }

    @GetMapping("/movie")
    @Cacheable(value = "movies")
    @Operation(summary = "Получение всех фильмов", description = "Получает список фильмов")
    @ApiResponse(responseCode = "200", description = "Возвращает список фильмов с пагинацией",
        content = @Content(mediaType = "application.json",
        schema = @Schema(implementation = PageDto.class)))
    public ResponseEntity<?> findAllMovies(
            @Parameter (description = "страница пагинации", required = true, example = "1")
            @RequestParam Integer page) {
        return ResponseEntity.ok().body(cinemaService.getMoviePageDto(page));
    }

    @GetMapping("/session")
    @Cacheable(value = "sessions")
    @ApiResponse(responseCode = "200", description = "Показывает список сеансов с пагинацией",
            content = @Content(mediaType = "application.json",
                    schema = @Schema(implementation = PageDto.class)))
    public ResponseEntity<?> findAllSessions(
            @Parameter (description = "страница пагинации", required = true, example = "1")
            @RequestParam Integer page) {
        return ResponseEntity.ok().body(cinemaService.getSessionPageDto(page));
    }

    @GetMapping("/session/{sessionId}/free-place")
    @Cacheable(value = "tickets")
    @ApiResponse(responseCode = "200", description = "Возвращает список свободных мест на сеансе. Принимает id сессии",
            content = @Content(array = @ArraySchema(schema = @Schema(example = "A1"))))
    public ResponseEntity<?> findFreeSeatsBySession(
            @Parameter (description = "id сеанса", required = true, example = "2")
            @PathVariable Integer sessionId) {
        return ResponseEntity.ok().body(cinemaService.getFreeTickets(sessionId));
    }

    @PostMapping("ticket/booking")
    @CacheEvict(value = "tickets", allEntries = true)
    @ApiResponse(responseCode = "200", description = "Успешная покупка билета",
        content = @Content(mediaType = "application.json", schema = @Schema(implementation = TicketDto.class)))
    @ApiResponse(responseCode = "409", description = "Билет с указанным id уже куплен")
    @ApiResponse(responseCode = "404", description = "Место с таким номером не найдено")
    public ResponseEntity<?> buyTicket(
            @RequestBody BuyTicketRequest request) {
        return ResponseEntity.ok().body(cinemaService.buyTicket(request));
    }

    private Boolean isTokenCorrect(String headerValue) {
        String adminToken = appConfig.appProperties().getHeaderValue();
        return adminToken.equals(headerValue);
    }

}
