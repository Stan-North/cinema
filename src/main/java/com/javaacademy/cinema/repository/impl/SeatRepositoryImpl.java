package com.javaacademy.cinema.repository.impl;

import com.javaacademy.cinema.entity.Seat;
import com.javaacademy.cinema.repository.SeatRepository;
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
public class SeatRepositoryImpl implements SeatRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM seat WHERE id = ?;";
    private static final String SELECT_BY_TITLE_SQL = "SELECT * FROM seat WHERE title = ?;";
    private static final String SELECT_ALL_SQL = "SELECT * FROM seat";
    private static final String SELECT_FREE_SEATS_TITLES_BY_SESSION_ID_SQL = """
    SELECT title
    FROM seat JOIN ticket on seat.id = ticket.seat_id
    WHERE session_id = ? AND is_bought = ?;
    """;

    @Override
    public Optional<Seat> findById(Integer id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, this::mapToSeat, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Seat> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, this::mapToSeat);
    }

    @Override
    public List<String> getFreeSeats(Integer sessionId) {
        return jdbcTemplate.queryForList(
                SELECT_FREE_SEATS_TITLES_BY_SESSION_ID_SQL,
                String.class, sessionId, false);
    }

    @SneakyThrows
    private Seat mapToSeat(ResultSet rs, int rowNum) {
        Seat seat = new Seat();
        seat.setId(rs.getInt("id"));
        seat.setTitle(rs.getString("title"));
        return seat;
    }

    @Override
    public Optional<Seat> findByTitle(String title) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_TITLE_SQL, this::mapToSeat, title));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
