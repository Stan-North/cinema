package com.javaacademy.cinema.service.impl;

import com.javaacademy.cinema.dto.PageDto;
import com.javaacademy.cinema.service.PaginationService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PaginationServiceImpl implements PaginationService {
    private static final int PAGE_SIZE = 10;
    private static final int DECREMENT_FOR_PAGINATION = 1;

    @Override
    public <T> PageDto<T> getPageDto(List<T> items, Comparator<T> comparator, Integer pageNumber) {
        int pageIndex = pageNumber - DECREMENT_FOR_PAGINATION;
        List<T> contentPerPage = items.stream()
                .sorted(comparator)
                .skip(PAGE_SIZE * pageIndex)
                .limit(PAGE_SIZE)
                .toList();
        int amountOfPages = items.size() / PAGE_SIZE;
        return new PageDto<>(contentPerPage, pageNumber, amountOfPages, PAGE_SIZE, contentPerPage.size());
    }
}
