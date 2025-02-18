package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.BuyTicketRequest;
import com.javaacademy.cinema.dto.TicketDto;

import java.util.List;

public interface TicketService {
    List<TicketDto> findBoughtTickets(Integer sessionId);
    TicketDto buyTicket(BuyTicketRequest request);
}
