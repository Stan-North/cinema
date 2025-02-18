package com.javaacademy.cinema.repository.impl;

import com.javaacademy.cinema.common.ErrorMessages;
import com.javaacademy.cinema.entity.Seat;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.TicketDoesNotExistException;
import com.javaacademy.cinema.exception.TicketStatusException;
import com.javaacademy.cinema.repository.SeatRepository;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.repository.TicketRepository;
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
public class TicketRepositoryImpl implements TicketRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SessionRepository sessionRepository;
    private final SeatRepository seatRepository;
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM ticket WHERE id = ?;";
    private static final String INSERT_TICKET_SQL = """
                INSERT INTO ticket (seat_id, session_id)
                VALUES (?, ?)
                RETURNING id;
                """;
    private static final String SELECT_BY_STATUS = """
                SELECT *
                FROM ticket
                WHERE session_id = ? AND is_bought = ?;
                """;
    private static final String UPDATE_STATUS = """
                UPDATE ticket
                SET is_bought = true
                WHERE id = ?;
                """;
    private static final String SELECT_ID = """
            SELECT id
            from ticket
            WHERE seat_id = ? AND session_id = ?;
            """;

    @Override
    public Optional<Ticket> findById(Integer id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, this::mapToTicket, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Ticket save(Session session, Seat seat) {
        Integer returningId = jdbcTemplate.queryForObject(
                INSERT_TICKET_SQL,
                Integer.class,
                seat.getId(),
                session.getId());
        return new Ticket(returningId, session, seat, false);
    }

    @Override
    public List<Ticket> findBought(Session session) {
        return jdbcTemplate.query(
                SELECT_BY_STATUS,
                this::mapToTicket,
                session.getId(),
                true);
    }

    @Override
    public List<Ticket> findNotBought(Session session) {
        return jdbcTemplate.query(
                SELECT_BY_STATUS,
                this::mapToTicket,
                session.getId(),
                false);
    }

    @Override
    public void checkAndUpdateTicket(Integer id) {
        Ticket ticket = findById(id).orElseThrow(TicketDoesNotExistException::new);
        if (!ticket.getIsBought()) {
            ticket.setIsBought(true);
            updateTicketStatus(id);
        } else {
            throw new TicketStatusException(ErrorMessages.TICKET_ALREADY_BOUGHT);
        }
    }

    private void updateTicketStatus(Integer id) {
        int countRows = jdbcTemplate.update(UPDATE_STATUS, ps -> ps.setInt(1, id));
        if (countRows < 1) {
            throw new RuntimeException(ErrorMessages.NO_ONE_ROWS_UPDATED);
        }
    }

    @Override
    public Integer findTicketId(Integer seatId, Integer sessionId) {
        return jdbcTemplate.queryForObject(
                SELECT_ID,
                Integer.class,
                seatId,
                sessionId);
    }

    @SneakyThrows
    private Ticket mapToTicket(ResultSet rs, int rowNum) {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setIsBought(rs.getBoolean("is_bought"));
        if (rs.getString("session_id") != null) {
            Integer sessionId = Integer.valueOf(rs.getString("session_id"));
            ticket.setSession(sessionRepository.finById(sessionId).orElse(null));
        }
        if (rs.getString("seat_id") != null) {
            Integer seatId = Integer.valueOf(rs.getString("seat_id"));
            ticket.setSeat(seatRepository.findById(seatId).orElse(null));
        }
        return ticket;
    }
}
