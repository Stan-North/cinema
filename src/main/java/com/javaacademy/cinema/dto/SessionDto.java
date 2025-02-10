package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"id", "movie_name", "date", "price"})
public class SessionDto {
    private Integer id;
    @JsonProperty(value = "movie_name")
    private String movieName;
    private LocalDateTime date;
    private BigDecimal price;
}