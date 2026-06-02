package com.lxjl.scaffold.common.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

/**
 * 通用分页出参。通常作为 {@code ApiResult#data} 的载荷。
 */
@JsonInclude(Include.NON_NULL)
public record PageResult<T>(
        List<T> content,
        long total,
        int page,
        int size,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {

    public static <T> PageResult<T> empty(PageQuery q) {
        return new PageResult<>(List.of(), 0, q.page(), q.size(), 0, false, false);
    }
}
