package com.javaacademy.cinema.repository;

import com.javaacademy.cinema.dto.SaveMovieRequest;
import com.javaacademy.cinema.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    Optional<Movie> findById(Integer id);
    Movie saveMovie(SaveMovieRequest dto);
    List<Movie> findAll();
}
