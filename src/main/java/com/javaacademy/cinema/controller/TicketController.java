package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.BuyTicketRequest;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.service.TicketService;
import com.javaacademy.cinema.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "TicketController")
@RequestMapping("/ticket")
public class TicketController {
    private final ValidationService validationService;
    private final TicketService ticketService;
    private static final String HEADER_NAME = "user-token";

    @GetMapping("/saled/{sessionId}")
    @Operation(summary = "Получение купленных билетов на сеанс", description = "должен быть указан id сеанса в пути + "
            + "Должен быть заголовок админа с токеном")
    @ApiResponse(responseCode = "200", description = "Возвращает список купленных билетов на указанный сеанс",
            content = @Content(mediaType = "application.json",
                    array = @ArraySchema(schema = @Schema(implementation = Ticket.class))))
    @ApiResponse(responseCode = "403", description = "Неверный токен пользователя")
    public ResponseEntity<?> getBoughtTickets(
            @Parameter(description = "Токен авторизации", required = true, example = "user-token <token>")
            @RequestHeader(value = HEADER_NAME) String userToken,
            @Parameter (description = "id сеанса", required = true, example = "1")
            @PathVariable Integer sessionId) {
        validationService.validateUserToken(userToken);
        return ResponseEntity.ok(ticketService.findBoughtTickets(sessionId));
    }

    @PostMapping("/booking")
    @CacheEvict(value = "tickets", allEntries = true)
    @ApiResponse(responseCode = "200", description = "Успешная покупка билета",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = TicketDto.class)))
    @ApiResponse(responseCode = "409", description = "Билет с указанным id уже куплен")
    @ApiResponse(responseCode = "404", description = "Место с таким номером не найдено")
    public ResponseEntity<?> buyTicket(
            @RequestBody BuyTicketRequest request) {
        return ResponseEntity.ok().body(ticketService.buyTicket(request));
    }
}
