package com.example.scaffold.common.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页与排序入参。页码为 <strong>0 起算</strong>，与 {@link Pageable} 一致。
 */
public record PageQuery(int page, int size, Sort sort) {

    public PageQuery {
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (sort == null) {
            sort = Sort.unsorted();
        }
    }

    public long offset() {
        return (long) page * (long) size;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, sort);
    }
}
