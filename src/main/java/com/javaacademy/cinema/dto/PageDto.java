package com.javaacademy.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageDto<T> {
    private List<T> content;
    private Integer pageNumber;
    private Integer amountOfPages;
    private Integer maxPageSize;
    private Integer currentSize;
}
