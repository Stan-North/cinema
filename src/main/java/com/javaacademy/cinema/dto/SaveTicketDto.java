package com.javaacademy.cinema.dto;

import com.javaacademy.cinema.entity.Seat;
import com.javaacademy.cinema.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTicketDto {
    private Session session;
    private Seat seat;
    private Boolean isBought;
}
