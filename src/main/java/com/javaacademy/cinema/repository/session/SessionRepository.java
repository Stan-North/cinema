package com.javaacademy.cinema.repository.session;

import com.javaacademy.cinema.dto.SaveSessionDto;
import com.javaacademy.cinema.entity.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository {
    Optional<Session> finById(Integer id);
    Optional<List<Session>> findAll();
    Session saveSession(SaveSessionDto dto);
}
