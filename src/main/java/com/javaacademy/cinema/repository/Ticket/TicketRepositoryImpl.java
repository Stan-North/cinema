package com.javaacademy.cinema.repository.Ticket;

import com.javaacademy.cinema.common.ErrorMessages;
import com.javaacademy.cinema.dto.SaveTicketDto;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.TicketDoesNotExist;
import com.javaacademy.cinema.exception.TicketStatusException;
import com.javaacademy.cinema.repository.seat.SeatRepository;
import com.javaacademy.cinema.repository.session.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.relational.core.sql.Select;
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

    @Override
    public Optional<Ticket> findById(Integer id) {
        String sql = """
                SELECT  *
                FROM ticket
                WHERE id = ?;
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, this::mapToTicket, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Ticket save(SaveTicketDto dto) {
        String sql = """
                INSERT INTO ticket (seat_id, session_id)
                VALUES (?, ?)
                RETURNING id;
                """;
        Integer returningId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                dto.getSeat().getId(),
                dto.getSession().getId());
        return new Ticket(returningId, dto.getSession(), dto.getSeat(), false);
    }

    @Override
    public List<Ticket> findBought(Session session) {
        String sql = """
                SELECT *
                FROM ticket
                WHERE session_id = ? AND is_bought = ?;
                """;
        return jdbcTemplate.query(
                sql,
                this::mapToTicket,
                session.getId(),
                true);
    }

    @Override
    public List<Ticket> findNotBought(Session session) {
        String sql = """
                SELECT *
                FROM ticket
                WHERE session_id = ? AND is_bought = ?;
                """;
        return jdbcTemplate.query(
                sql,
                this::mapToTicket,
                session.getId(),
                false);
    }

    @Override
    public void checkAndUpdateTicket(Integer id) {
        Ticket ticket = findById(id).orElseThrow(TicketDoesNotExist::new);
        if (!ticket.getIsBought()) {
            ticket.setIsBought(true);
            updateTicketStatus(id);
        } else {
            throw new TicketStatusException(ErrorMessages.TICKET_ALREADY_BOUGHT);
        }
    }

    private void updateTicketStatus(Integer id) {
        String sql = """
                UPDATE ticket
                SET is_bought = true
                WHERE id = ?;
                """;
        int countRows = jdbcTemplate.update(sql, ps -> ps.setInt(1, id));
        if (countRows < 1) {
            throw new RuntimeException(ErrorMessages.NO_ONE_ROWS_UPDATED);
        }
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
