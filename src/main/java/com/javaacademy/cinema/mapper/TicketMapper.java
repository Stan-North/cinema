package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    public TicketDto toDto(Ticket ticket) {
        return new TicketDto(ticket.getId(),
                ticket.getSeat().getTitle(),
                ticket.getSession().getMovie().getName(),
                ticket.getSession().getDate());
    }

}
