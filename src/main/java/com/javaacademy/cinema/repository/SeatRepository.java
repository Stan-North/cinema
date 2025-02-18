package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.entity.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    Optional<Seat> findById(Integer id);
    List<Seat> findAll();
    List<String> getFreeSeats(Integer sessionId);
    Optional<Seat> findByTitle(String title);
}
