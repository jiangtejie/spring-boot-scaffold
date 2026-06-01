package com.example.scaffold.common.converter;

import com.example.scaffold.common.page.PageResult;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DTO 转换工具 — 实体→DTO 批量 / 分页转换。
 */
public final class DtoConverter {

    private DtoConverter() {}

    public static <E, D> D convert(E entity, Function<E, D> converter) {
        return entity == null ? null : converter.apply(entity);
    }

    public static <E, D> List<D> convertList(List<E> entities, Function<E, D> converter) {
        if (entities == null || entities.isEmpty()) return List.of();
        return entities.stream().map(converter).collect(Collectors.toList());
    }

    public static <E, D> List<D> convertListNonNull(List<E> entities, Function<E, D> converter) {
        if (entities == null || entities.isEmpty()) return List.of();
        return entities.stream().filter(e -> e != null).map(converter).collect(Collectors.toList());
    }

    public static <E, D> PageResult<D> convertPage(PageResult<E> page, Function<E, D> converter) {
        if (page == null) return null;
        return new PageResult<>(
                convertList(page.content(), converter),
                page.total(), page.page(), page.size(),
                page.totalPages(), page.hasNext(), page.hasPrevious());
    }
}
