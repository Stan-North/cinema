package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.MovieAdminDto;
import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveMovieRequest;


import java.util.List;

public interface MovieService {
    MovieAdminDto saveMovie(SaveMovieRequest dto);
    List<MovieDto> findAllMovies();
    PageDto<MovieDto> getMovies(Integer pageNumber);
}
