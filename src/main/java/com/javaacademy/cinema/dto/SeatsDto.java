package com.javaacademy.cinema.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Содержит информацию о фильме для админа")
public class SeatsDto {
    @Schema(description = "Список свободных мест на сеанс")
    private List<String> freeSeats;
}
