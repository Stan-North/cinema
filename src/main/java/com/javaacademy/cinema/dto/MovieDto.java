package com.javaacademy.cinema.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Содержит информацию о фильме")
public class MovieDto {
    @Schema(description = "Название фильма")
    private String name;
    @Schema(description = "Описание фильма")
    private String description;
}
