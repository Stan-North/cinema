package com.javaacademy.cinema;

import com.javaacademy.cinema.dto.SaveSessionDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.repository.movie.MovieRepositoryImpl;
import com.javaacademy.cinema.repository.session.SessionRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
public class CinemaApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CinemaApplication.class, args);
		MovieRepositoryImpl movieRepository = context.getBean(MovieRepositoryImpl.class);
		Optional<Movie> movie = movieRepository.findById(2);
		//System.out.println(movie);

		SessionRepositoryImpl sessionRepository = context.getBean(SessionRepositoryImpl.class);
		//Optional<Session> session = sessionRepository.finById(1);
		SaveSessionDto saveSessionDto = new SaveSessionDto(movie.orElseThrow(), LocalDateTime.now(),
				BigDecimal.valueOf(500));
		Session session1 = sessionRepository.saveSession(saveSessionDto);
		System.out.println(session1);
	}

}
