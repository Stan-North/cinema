package com.javaacademy.cinema.repository.seat;

import com.javaacademy.cinema.entity.Seat;
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

    @Override
    public Optional<Seat> findById(Integer id) {
        String sql = """
                SELECT *
                FROM seat
                WHERE id = ?;
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, this::mapToSeat, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Seat> findAll() {
        String sql = """
                SELECT *
                FROM seat;
                """;
        return jdbcTemplate.query(sql, this::mapToSeat);
    }

    @SneakyThrows
    private Seat mapToSeat(ResultSet rs, int rowNum) {
        Seat seat = new Seat();
        seat.setId(rs.getInt("id"));
        seat.setTitle(rs.getString("title"));
        return seat;
    }
}
