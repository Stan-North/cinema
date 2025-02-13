package com.javaacademy.cinema.dto;

import com.javaacademy.cinema.entity.Seat;
import com.javaacademy.cinema.entity.Session;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTicketDto {
    @Schema(description = "Сеанс")
    private Session session;
    @Schema(description = "Номер места")
    private Seat seat;
}
