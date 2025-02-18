package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.PageDto;

import java.util.Comparator;
import java.util.List;

public interface PaginationService {
    <T> PageDto<T> getPageDto(List<T> items, Comparator<T> comparator, Integer pageNumber);
}
