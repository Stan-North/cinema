package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"id", "movie_name", "date", "price"})
@Schema(description = "Содержит информацию о сеансе")
public class SessionDto {
    @Schema(description = "Id сеанса")
    private Integer id;
    @JsonProperty(value = "movie_name")
    @Schema(description = "Название фильма")
    private String movieName;
    @Schema(description = "Время и дата фильма")
    private LocalDateTime date;
    @Schema(description = "Стоимость")
    private BigDecimal price;
}
