package com.javaacademy.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveMovieRequest {
    private String name;
    private String description;
}
