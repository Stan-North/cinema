package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Seat;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> findById(Integer id);
    Ticket save(Session session, Seat seat);
    void checkAndUpdateTicket(Integer id);
    List<Ticket> findBought(Session session);
    List<Ticket> findNotBought(Session session);
    Integer findTicketId(Integer seatId, Integer sessionId);
}
