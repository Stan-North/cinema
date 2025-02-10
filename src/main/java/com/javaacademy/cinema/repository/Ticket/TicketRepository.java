package com.javaacademy.cinema.repository.Ticket;

import com.javaacademy.cinema.dto.SaveTicketDto;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> findById(Integer id);
    Ticket save(SaveTicketDto dto);
    void checkAndUpdateTicket(Integer id);
    List<Ticket> findBought(Session session);
    List<Ticket> findNotBought(Session session);
    Integer findTicketId(Integer seatId, Integer sessionId);
}
