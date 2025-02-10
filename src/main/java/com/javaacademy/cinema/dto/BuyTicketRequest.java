package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuyTicketRequest {
    @JsonProperty(value = "session_id")
    private final Integer sessionId;
    @JsonProperty(value = "place_name")
    private final String seatTitle;
}
