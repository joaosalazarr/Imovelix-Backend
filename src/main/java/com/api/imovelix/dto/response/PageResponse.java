package com.api.imovelix.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T> (
    List<T> content,
    Integer totalPages,
    Long totalElements,
    Integer size,
    Integer number,
    Integer numberOfElements,
    Boolean first,
    Boolean last,
    Boolean empty
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.getSize(),
            page.getNumber(),
            page.getNumberOfElements(),
            page.isFirst(),
            page.isLast(),
            page.isEmpty()
        );
    }
}
