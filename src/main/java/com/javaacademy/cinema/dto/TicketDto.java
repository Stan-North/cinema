package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"ticket_id", "place_name", "movie_name", "date"})
public class TicketDto {
    @JsonProperty(value = "ticket_id")
    private Integer ticketId;
    @JsonProperty(value = "place_name")
    private String seatTitle;
    @JsonProperty(value = "movie_name")
    private String movieName;
    private LocalDateTime date;
}
