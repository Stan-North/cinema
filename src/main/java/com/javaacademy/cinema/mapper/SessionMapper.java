package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {
    public SessionDto toDto(Session session) {
        return  new SessionDto(session.getId(), session.getMovie().getName(), session.getDate(), session.getPrice());
    }
}
