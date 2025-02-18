package com.javaacademy.cinema;

import com.javaacademy.cinema.dto.*;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.mapper.MovieMapper;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.repository.MovieRepository;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("local")
class ControllerTest {
    private static final String HEADER_NAME = "user-token";
    private static final String HEADER_VAlUE = "secretadmin123";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private SessionMapper sessionMapper;

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setPort(8080)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();


    @Test
    @DisplayName("Успешное сохранение фильма")
    void saveMovieSuccess() {
        String filmName = "Тестовое название фильма1";
        String filmDescription = "Тестовое описание фильма1";
        SaveMovieRequest saveMovieRequest = new SaveMovieRequest(filmName, filmDescription);
        Header header = new Header(HEADER_NAME, HEADER_VAlUE);
        Movie response = RestAssured.given(requestSpecification)
                .body(saveMovieRequest)
                .header(header)
                .post("/movie")
                .then()
                .spec(responseSpecification)
                .statusCode(201)
                .extract()
                .body()
                .as(Movie.class);

        int idFromResponse = response.getId();
        Movie movieFromBD = movieRepository.findById(idFromResponse).orElseThrow(RuntimeException::new);

        Assertions.assertEquals(filmName, response.getName());
        Assertions.assertEquals(filmDescription, response.getDescription());
        Assertions.assertEquals(movieFromBD.getName(), response.getName());
        Assertions.assertEquals(movieFromBD.getDescription(), response.getDescription());
    }

    @Test
    @DisplayName("Неудача при добавлении фильма с неверным токеном")
    void saveMovieFailure() {
        String filmName = "Тестовое название фильма1";
        String filmDescription = "Тестовое описание фильма1";
        SaveMovieRequest saveMovieRequest = new SaveMovieRequest(filmName, filmDescription);
        Header header = new Header(HEADER_NAME, "wrongtoken");
        RestAssured.given(requestSpecification)
                .body(saveMovieRequest)
                .header(header)
                .post("/movie")
                .then()
                .spec(responseSpecification)
                .statusCode(403);
    }

    @Test
    @DisplayName("Успешное добавление сессии")
    void saveSessionSuccess() {
        LocalDateTime time = LocalDateTime.now();
        int movieId = 1;
        SaveSessionRequest saveSessionRequest = new SaveSessionRequest(movieId, time, BigDecimal.valueOf(100));
        Header header = new Header(HEADER_NAME, HEADER_VAlUE);
        Session response = RestAssured.given(requestSpecification)
                .body(saveSessionRequest)
                .header(header)
                .post("/session")
                .then()
                .spec(responseSpecification)
                .statusCode(201)
                .extract()
                .body()
                .as(Session.class);
        Movie movieFromBd = movieRepository.findById(movieId).orElseThrow(RuntimeException::new);
        Session sessionFromBd = sessionRepository.finById(response.getId()).orElseThrow(RuntimeException::new);
        Assertions.assertEquals(sessionFromBd.getId(), response.getId());
        Assertions.assertEquals(sessionFromBd.getDate(), response.getDate());
        Assertions.assertEquals(sessionFromBd.getMovie(), movieFromBd);
        Assertions.assertEquals(
                sessionFromBd.getPrice(),
                response.getPrice().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("Неудача при сохранении сессии с неправильным токеном")
    void saveSessionFailure() {
        LocalDateTime time = LocalDateTime.now();
        int movieId = 1;
        SaveSessionRequest saveSessionRequest = new SaveSessionRequest(movieId, time, BigDecimal.valueOf(100));
        Header header = new Header(HEADER_NAME, "wrongvalue");
        RestAssured.given(requestSpecification)
                .body(saveSessionRequest)
                .header(header)
                .post("/session")
                .then()
                .spec(responseSpecification)
                .statusCode(403);
    }

    @Test
    @DisplayName("Получение купленных билетов на сеанс")
    void getBoughtTickets() {
        Session session = sessionRepository.finById(1).orElseThrow(() -> new RuntimeException("Сессия не найдена"));
        Header header = new Header(HEADER_NAME, HEADER_VAlUE);
        List<Ticket> boughtTicketsFromDb = ticketRepository.findBought(session);
        List<Ticket> responseTickets = RestAssured.given(requestSpecification)
                .header(header)
                .get("/ticket/saled/1")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        Assertions.assertEquals(boughtTicketsFromDb, responseTickets);
    }



    @Test
    @DisplayName("Неудача получения при неправильном токен")
    void getBoughtTicketsFailure() {
        Header header = new Header(HEADER_NAME, "wrongValue");
        RestAssured.given(requestSpecification)
                .header(header)
                .get("/ticket/saled/1")
                .then()
                .spec(responseSpecification)
                .statusCode(403);
    }

    @Test
    @DisplayName("Успешное получение фильмов")
    void getAllMoviesSuccess() {
        PageDto<MovieDto> response = RestAssured.given(requestSpecification)
                .get("/movie?page=1")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        List<MovieDto> contentFromResponse = response.getContent();
        List<Movie> allMoviesFromDb = movieRepository.findAll();
        List<MovieDto> dtos = allMoviesFromDb.stream()
                .map(movieMapper::toDto)
                .limit(10)
                .toList();
        Assertions.assertEquals(contentFromResponse, dtos);
    }

    @Test
    @DisplayName("Получение всех сеансов")
    void getAllSessionsSuccess() {
        PageDto<SessionDto> response = RestAssured.given(requestSpecification)
                .get("/session?page=1")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        List<SessionDto> sessionFromResponse = response.getContent();
        List<Session> allSessionFromDb = sessionRepository.findAll();
        List<SessionDto> dtos = allSessionFromDb.stream().limit(10)
                .map(sessionMapper::toDto)
                .toList();
        Assertions.assertEquals(sessionFromResponse, dtos);
    }

    @Test
    @DisplayName("Получение свободных места на сеансе")
    void getFreeTicketsBySessionId() {
        Session sessionFromDb = sessionRepository.finById(1).
                orElseThrow(() -> new RuntimeException("Сессия не найдена"));
        List<String> response = RestAssured.given(requestSpecification)
                .get("/session/1/free-place")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });

        List<Ticket> freeTicketsFromDb = ticketRepository.findNotBought(sessionFromDb);
        List<String> freeSeatFromDb = freeTicketsFromDb.stream().map(ticket -> ticket.getSeat().getTitle()).toList();
        Assertions.assertEquals(response, freeSeatFromDb);
    }

    @Test
    @DisplayName("Успешная покупка билета")
    void buyTicketSuccess() {
        jdbcTemplate.update("UPDATE ticket SET is_bought = false WHERE session_id = 1");
        BuyTicketRequest buyTicketRequest = new BuyTicketRequest(1, "A1");
        TicketDto ticketDto = RestAssured.given(requestSpecification)
                .body(buyTicketRequest)
                .post("/ticket/booking")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(TicketDto.class);

        Session session = sessionRepository.finById(1).orElseThrow(() -> new RuntimeException("Сессия не найдена"));
        Ticket ticketFromDb = ticketRepository.findById(ticketDto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Билет не найден"));

        Assertions.assertTrue(ticketFromDb.getIsBought());
        Assertions.assertEquals(ticketDto.getDate(), session.getDate());
        Assertions.assertEquals(ticketDto.getMovieName(), session.getMovie().getName());
        Assertions.assertEquals(ticketDto.getSeatTitle(), ticketFromDb.getSeat().getTitle());
    }

    @Test
    @DisplayName("Неудача при попытке купить купленный билет")
    void buyTicketFailure() {
        BuyTicketRequest buyTicketRequest = new BuyTicketRequest(1, "A1");
        RestAssured.given(requestSpecification)
                .body(buyTicketRequest)
                .post("/ticket/booking")
                .then()
                .spec(responseSpecification)
                .statusCode(409);
    }
}
