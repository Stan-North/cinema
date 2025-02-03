package com.javaacademy.cinema.repository.movie;

import com.javaacademy.cinema.dto.SaveMovieDto;
import com.javaacademy.cinema.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Movie> findById(Integer id) {
        String sql = """
                SELECT *
                FROM movie
                WHERE id = ?;
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, this::mapToMovie, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Movie saveMovie(SaveMovieDto dto) {
        String sql = """
              INSERT INTO movie (name, description) 
               VALUES (?, ?)
               RETURNING id;
                """;
        Integer returningId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                dto.getName(),
                dto.getDescription());
        return new Movie(returningId, dto.getName(), dto.getDescription());
    }

    @Override
    public Optional<List<Movie>> findAll() {
        String sql = """
                SELECT *
                FROM movies;
                """;
        try {
            return Optional.of(jdbcTemplate.query(sql, this::mapToMovie));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @SneakyThrows
    private Movie mapToMovie(ResultSet rs, int rowNum) {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setName(rs.getString("name"));
        movie.setDescription(rs.getString("description"));
        return movie;
    }

}
