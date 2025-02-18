package com.javaacademy.cinema.service.impl;

import com.javaacademy.cinema.dto.SeatsDto;
import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveSessionRequest;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Seat;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.SessionDoesNotExistException;
import com.javaacademy.cinema.mapper.SeatMapper;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.repository.SeatRepository;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import com.javaacademy.cinema.service.PaginationService;
import com.javaacademy.cinema.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final SeatMapper seatMapper;
    private final PaginationService paginationService;

    @Override
    public SessionDto saveSession(SaveSessionRequest request) {
        Session session = sessionRepository.saveSession(request);
        List<Seat> seatList = seatRepository.findAll();
        seatList.forEach(seat -> ticketRepository.save(session, seat));
        return sessionMapper.toDto(session);
    }

    @Override
    public List<SessionDto> findAllSession() {
        return sessionRepository.findAll()
                .stream()
                .map(sessionMapper::toDto)
                .toList();
    }

    @Override
    public PageDto<SessionDto> getSessions(Integer pageNumber) {
        List<SessionDto> allSession = findAllSession();
        return paginationService.getPageDto(allSession, Comparator.comparing(SessionDto::getId), pageNumber);
    }

    @Override
    public List<String> getFreeSeats(Integer sessionId) {
        return seatRepository.getFreeSeats(sessionId);
    }
}
