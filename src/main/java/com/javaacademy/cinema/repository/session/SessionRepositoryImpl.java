package com.javaacademy.cinema.repository.session;

import com.javaacademy.cinema.dto.SaveSessionDto;
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

    @Override
    public Optional<Session> finById(Integer id) {
        String sql = """
                SELECT *
                FROM session
                WHERE id = ?;
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, this::mapToSession, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Session>> findAll() {
        String sql = """
                SELECT *
                FROM session;
                """;
        try {
            return Optional.of(jdbcTemplate.query(sql, this::mapToSession));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Session saveSession(SaveSessionDto dto) {
        String sql = """
                INSERT INTO session (movie_id, date, price)
                VALUES (?, ?, ?)
                RETURNING id;
                """;
        Integer sessionId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                dto.getMovie().getId(),
                dto.getDate(),
                dto.getPrice());
        return new Session(sessionId, dto.getMovie(), dto.getDate(), dto.getPrice());
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
