package com.javaacademy.cinema.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Содержит список объектов и информацию по пагинации")
public class PageDto<T> {
    @Schema(description = "Список объектов на текущей странице")
    private List<T> content;
    @Schema(description = "Номер текущей страницы")
    private Integer pageNumber;
    @Schema(description = "Количество страниц")
    private Integer amountOfPages;
    @Schema(description = "Размер страницы")
    private Integer maxPageSize;
    @Schema(description = "Объектов на текущей странице")
    private Integer currentSize;
}
