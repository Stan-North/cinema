package com.javaacademy.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveMovieDto {
    private String name;
    private String description;
}
