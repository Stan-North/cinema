package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.BuyTicketRequest;
import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveMovieRequest;
import com.javaacademy.cinema.dto.SaveSessionRequest;
import com.javaacademy.cinema.dto.SaveTicketDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Seat;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.SeatDoesNotExistException;
import com.javaacademy.cinema.exception.SessionDoesNotExistException;
import com.javaacademy.cinema.mapper.MovieMapper;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.repository.ticket.TicketRepository;
import com.javaacademy.cinema.repository.movie.MovieRepository;
import com.javaacademy.cinema.repository.seat.SeatRepository;
import com.javaacademy.cinema.repository.session.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {
    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final MovieMapper movieMapper;
    private final SessionMapper sessionMapper;
    private static final int PAGE_SIZE = 10;
    private static final int DECREMENT_FOR_PAGINATION = 1;

    @Override
    public Movie saveMovie(SaveMovieRequest dto) {
        return movieRepository.saveMovie(dto);
    }

    @Override
    public Session saveSession(SaveSessionRequest request) {
        Session session = sessionRepository.saveSession(request);
        List<Seat> seatList = seatRepository.findAll();
        seatList.forEach(seat -> ticketRepository.save(new SaveTicketDto(session, seat)));
        return session;
    }

    @Override
    public List<Ticket> findBoughtTickets(Integer sessionId) {
        Session session = sessionRepository.finById(sessionId).orElseThrow(SessionDoesNotExistException::new);
        return ticketRepository.findBought(session);
    }

    @Override
    public List<MovieDto> findAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public List<SessionDto> findAllSession() {
        return sessionRepository.findAll()
                .stream()
                .map(sessionMapper::toDto)
                .toList();
    }

    @Override
    public PageDto<MovieDto> getMoviePageDto(Integer pageNumber) {
        List<MovieDto> allMovies = findAllMovies();
        return getPageDto(allMovies, Comparator.comparing(MovieDto::getName), pageNumber);
    }

    @Override
    public PageDto<SessionDto> getSessionPageDto(Integer pageNumber) {
        List<SessionDto> allSession = findAllSession();
        return getPageDto(allSession, Comparator.comparing(SessionDto::getId), pageNumber);
    }

    @Override
    public List<String> getFreeTickets(Integer sessionId) {
        Session session = sessionRepository.finById(sessionId).orElseThrow(SessionDoesNotExistException::new);
        List<Ticket> notBought = ticketRepository.findNotBought(session);
        return notBought.stream()
                .map(ticket -> ticket.getSeat().getTitle())
                .toList();
    }

    @Override
    public TicketDto buyTicket(BuyTicketRequest request) {
        Session session = sessionRepository.finById(request.getSessionId())
                .orElseThrow(SessionDoesNotExistException::new);
        String seatTitle = request.getSeatTitle();
        Seat seat = seatRepository.findAll().stream()
                .filter(s -> Objects.equals(seatTitle, s.getTitle()))
                .findFirst()
                .orElseThrow(SeatDoesNotExistException::new);
        Integer seatId = seat.getId();
        Integer ticketId = ticketRepository.findTicketId(seatId, session.getId());
        ticketRepository.checkAndUpdateTicket(ticketId);
        return new TicketDto(ticketId, seatTitle, session.getMovie().getName(), session.getDate());
    }

    private <T> PageDto<T> getPageDto(List<T> items, Comparator<T> comparator, Integer pageNumber) {
        int pageIndex = pageNumber - DECREMENT_FOR_PAGINATION;
        List<T> contentPerPage = items.stream()
                .sorted(comparator)
                .skip(PAGE_SIZE * pageIndex)
                .limit(PAGE_SIZE)
                .toList();
        int amountOfPages = items.size() / PAGE_SIZE;
        return new PageDto<>(contentPerPage, pageNumber, amountOfPages, PAGE_SIZE, contentPerPage.size());
    }
}
