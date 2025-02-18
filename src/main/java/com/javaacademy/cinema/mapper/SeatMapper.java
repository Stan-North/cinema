package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.SeatsDto;
import com.javaacademy.cinema.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeatMapper {
    public SeatsDto toDto(List<Ticket> tickets) {
        List<String> freeSeatsTitles = tickets.stream().map(ticket -> ticket.getSeat().getTitle())
                .toList();
        return new SeatsDto(freeSeatsTitles);
    }
}
