package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.MovieAdminDto;
import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieDto toDto(Movie movie) {
        return new MovieDto(movie.getName(), movie.getDescription());
    }
    public MovieAdminDto toAdminDto(Movie movie) {
        return new MovieAdminDto(movie.getId(), movie.getName(), movie.getDescription());
    }
}
