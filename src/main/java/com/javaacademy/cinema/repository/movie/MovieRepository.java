package com.javaacademy.cinema.repository.movie;

import com.javaacademy.cinema.dto.SaveMovieDto;
import com.javaacademy.cinema.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    Optional<Movie> findById(Integer id);
    Movie saveMovie(SaveMovieDto dto);
    Optional<List<Movie>> findAll();
}
