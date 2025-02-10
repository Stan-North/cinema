package com.javaacademy.cinema.repository.session;

import com.javaacademy.cinema.dto.SaveSessionRequest;
import com.javaacademy.cinema.entity.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository {
    Optional<Session> finById(Integer id);
    List<Session> findAll();
    Session saveSession(SaveSessionRequest request);
}
