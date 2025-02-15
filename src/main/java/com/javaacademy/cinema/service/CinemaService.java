package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.BuyTicketRequest;
import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveMovieRequest;
import com.javaacademy.cinema.dto.SaveSessionRequest;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;

import java.util.List;

public interface CinemaService {
    Movie saveMovie(SaveMovieRequest dto);
    Session saveSession(SaveSessionRequest request);
    List<Ticket> findBoughtTickets(Integer sessionId);
    List<MovieDto> findAllMovies();
    List<SessionDto> findAllSession();
    PageDto<MovieDto> getMoviePageDto(Integer pageNumber);
    PageDto<SessionDto> getSessionPageDto(Integer pageNumber);
    List<String> getFreeTickets(Integer sessionId);
    TicketDto buyTicket(BuyTicketRequest request);
}
