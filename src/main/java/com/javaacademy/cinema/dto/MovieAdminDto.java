package com.javaacademy.cinema.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Содержит информацию о фильме для админа")
public class MovieAdminDto {
    @Schema(description = "Id фильма")
    private Integer id;
    @Schema(description = "Название фильма")
    private String name;
    @Schema(description = "Описание фильма")
    private String description;
}
