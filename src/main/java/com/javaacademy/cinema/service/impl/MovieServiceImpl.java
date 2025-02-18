package com.javaacademy.cinema.service.impl;

import com.javaacademy.cinema.dto.MovieAdminDto;
import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.dto.SaveMovieRequest;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.mapper.MovieMapper;
import com.javaacademy.cinema.repository.MovieRepository;
import com.javaacademy.cinema.service.MovieService;
import com.javaacademy.cinema.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final PaginationService paginationService;

    @Override
    public MovieAdminDto saveMovie(SaveMovieRequest dto) {
        Movie movie = movieRepository.saveMovie(dto);
        return movieMapper.toAdminDto(movie);
    }

    @Override
    public List<MovieDto> findAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public PageDto<MovieDto> getMovies(Integer pageNumber) {
        List<MovieDto> allMovies = findAllMovies();
        return paginationService.getPageDto(allMovies, Comparator.comparing(MovieDto::getName), pageNumber);
    }
}
