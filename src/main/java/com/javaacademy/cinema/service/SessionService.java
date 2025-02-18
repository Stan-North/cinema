package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.SeatsDto;
import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveSessionRequest;
import com.javaacademy.cinema.dto.SessionDto;

import java.util.List;

public interface SessionService {
    SessionDto saveSession(SaveSessionRequest request);
    List<SessionDto> findAllSession();
    PageDto<SessionDto> getSessions(Integer pageNumber);
    List<String> getFreeSeats(Integer sessionId);
}
