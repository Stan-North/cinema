package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"ticket_id", "place_name", "movie_name", "date"})
@Schema(description = "Содержит информацию о билете")
public class TicketDto {
    @JsonProperty(value = "ticket_id")
    @Schema(description = "id билета")
    private Integer ticketId;
    @JsonProperty(value = "place_name")
    @Schema(description = "Номер метса")
    private String seatTitle;
    @JsonProperty(value = "movie_name")
    @Schema(description = "Название фильма")
    private String movieName;
    @Schema(description = "Время и дата фильма")
    private LocalDateTime date;
}
