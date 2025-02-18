package com.javaacademy.cinema.service.impl;

import com.javaacademy.cinema.dto.BuyTicketRequest;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Seat;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.SeatDoesNotExistException;
import com.javaacademy.cinema.exception.SessionDoesNotExistException;
import com.javaacademy.cinema.mapper.TicketMapper;
import com.javaacademy.cinema.repository.SeatRepository;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import com.javaacademy.cinema.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final TicketMapper ticketMapper;
    private final SeatRepository seatRepository;

    @Override
    public List<TicketDto> findBoughtTickets(Integer sessionId) {
        Session session = sessionRepository.finById(sessionId).orElseThrow(SessionDoesNotExistException::new);
        List<Ticket> boughtTickets = ticketRepository.findBought(session);
        return boughtTickets.stream().map(ticketMapper::toDto).toList();
    }

    @Override
    public TicketDto buyTicket(BuyTicketRequest request) {
        Session session = sessionRepository.finById(request.getSessionId()).orElseThrow(SessionDoesNotExistException::new);
        Seat seat = seatRepository.findByTitle(request.getSeatTitle()).orElseThrow(SeatDoesNotExistException::new);
        Integer sessionId = request.getSessionId();
        Integer ticketId = ticketRepository.findTicketId(seat.getId(), sessionId);
        ticketRepository.checkAndUpdateTicket(ticketId);
        return new TicketDto(ticketId, seat.getTitle(), session.getMovie().getName(), session.getDate());
    }

}
