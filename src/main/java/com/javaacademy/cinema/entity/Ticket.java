package com.javaacademy.cinema.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Integer id;
    private Session session;
    private Seat seat;
    private Boolean isBought;
}
