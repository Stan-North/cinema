package com.javaacademy.cinema.dto;

import com.javaacademy.cinema.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SaveSessionDto {
    private Movie movie;
    private LocalDateTime date;
    private BigDecimal price;
}
