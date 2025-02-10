package com.javaacademy.cinema.repository.movie;

import com.javaacademy.cinema.dto.SaveMovieRequest;
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
    private static final String SELECT_BY_ID = "SELECT * FROM movie WHERE id = ?;";
    public static final String SELECT_ALL = "SELECT * FROM movie;";
    public static final String INSERT_MOVIE = "INSERT INTO movie (name, description) VALUES (?, ?) RETURNING id;";

    @Override
    public Optional<Movie> findById(Integer id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, this::mapToMovie, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Movie saveMovie(SaveMovieRequest dto) {
        Integer returningId = jdbcTemplate.queryForObject(
                INSERT_MOVIE,
                Integer.class,
                dto.getName(),
                dto.getDescription());
        return new Movie(returningId, dto.getName(), dto.getDescription());
    }

    @Override
    public List<Movie> findAll() {
        return jdbcTemplate.query(SELECT_ALL, this::mapToMovie);
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
