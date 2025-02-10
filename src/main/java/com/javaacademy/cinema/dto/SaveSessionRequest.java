package com.javaacademy.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SaveSessionRequest {
    private Integer movieId;
    private LocalDateTime date;
    private BigDecimal price;
}
