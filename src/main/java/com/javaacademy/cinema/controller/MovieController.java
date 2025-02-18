package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveMovieRequest;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.service.MovieService;
import com.javaacademy.cinema.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Movie Controller")
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;
    private final ValidationService validationService;
    private static final String HEADER_NAME = "user-token";

    @PostMapping()
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
        validationService.validateUserToken(userToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.saveMovie(request));
    }

    @GetMapping()
    @Cacheable(value = "movies")
    @Operation(summary = "Получение всех фильмов", description = "Получает список фильмов")
    @ApiResponse(responseCode = "200", description = "Возвращает список фильмов с пагинацией",
            content = @Content(mediaType = "application.json",
                    schema = @Schema(implementation = PageDto.class)))
    public ResponseEntity<?> findAllMovies(
            @Parameter (description = "страница пагинации", required = true, example = "1")
            @RequestParam Integer pageNumber) {
        return ResponseEntity.ok().body(movieService.getMovies(pageNumber));
    }
}
