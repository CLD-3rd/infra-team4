package com.cloudboot.room_reservation.util.global;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class PagedApiResponse<T> extends ApiResponse<List<T>> {
    private final int currentPage;
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final List<Integer> displayPages;

    private PagedApiResponse(List<T> data, int currentPage, int pageSize,
                             int totalPages, long totalElements) {
        super(data, "SUCCESS", null);
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.displayPages = calculateDisplayPagesFindAll(currentPage, totalPages);
    }

    public static <T> PagedApiResponse<T> of(Page<T> page) {
        return new PagedApiResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    private List<Integer> calculateDisplayPagesFindAll(int currentPage, int totalPages) {
        int startPage = Math.max(0, currentPage - 1);
        if (currentPage >= totalPages - 2) {
            startPage = Math.max(0, totalPages - 3);
        }
        int endPage = Math.min(startPage + 2, totalPages - 1);

        return IntStream.rangeClosed(startPage, endPage)
                .map(i -> i + 1) // 1-based로 변환
                .boxed()
                .collect(Collectors.toList());
    }
}