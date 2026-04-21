package it.mediflow.sportello.web.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResonse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements) {
    public static <T> PageResonse<T> of(Page<T> page) {
        return new PageResonse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
