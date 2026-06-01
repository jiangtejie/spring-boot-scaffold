package com.example.scaffold.common.page;

import java.util.List;

public final class PageResults {

    private PageResults() {}

    public static <T> PageResult<T> of(List<T> content, PageQuery query, long total) {
        int size = query.size();
        if (size < 1) throw new IllegalArgumentException("size must be >= 1");
        long totalPages = total == 0 ? 0 : (total + size - 1) / size;
        int tp = totalPages > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalPages;
        int p = query.page();
        boolean hasNext = p + 1 < tp;
        boolean hasPrevious = p > 0 && tp > 0;
        return new PageResult<>(content, total, p, size, tp, hasNext, hasPrevious);
    }
}
