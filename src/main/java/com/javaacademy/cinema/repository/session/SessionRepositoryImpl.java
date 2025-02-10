package com.javaacademy.cinema.repository.session;

import com.javaacademy.cinema.dto.SaveSessionRequest;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MovieRepository movieRepository;
    public static final String SELECT_BY_ID_SQL = " SELECT * FROM session WHERE id = ?;";
    public static final String SELECT_ALL_SQL = "SELECT * FROM session;";
    public static final String INSERT_SESSION_SQL = """
                INSERT INTO session (movie_id, date, price)
                VALUES (?, ?, ?)
                RETURNING id;
                """;

    @Override
    public Optional<Session> finById(Integer id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, this::mapToSession, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Session> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, this::mapToSession);
    }

    @Override
    public Session saveSession(SaveSessionRequest request) {
        Integer sessionId = jdbcTemplate.queryForObject(
                INSERT_SESSION_SQL,
                Integer.class,
                request.getMovieId(),
                request.getDate(),
                request.getPrice());
        Optional<Movie> movie = movieRepository.findById(request.getMovieId());
        return new Session(sessionId, movie.orElseThrow(), request.getDate(), request.getPrice());
    }

    @SneakyThrows
    private Session mapToSession(ResultSet rs, int rowNum) {
        Session session = new Session();
        session.setId(rs.getInt("id"));
        if (rs.getString("movie_id") != null) {
            Integer movieId = Integer.valueOf(rs.getString("movie_id"));
            session.setMovie(movieRepository.findById(movieId).orElse(null));
        }
        session.setDate(rs.getTimestamp("date").toLocalDateTime());
        session.setPrice(rs.getBigDecimal("price"));
        return session;
    }
}
